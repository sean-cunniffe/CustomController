package com.customcontroller.services;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.TokenType;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.customcontroller.services.AuthFilter.Method.*;

/**
 *
 * <p>Adds endpoint security using regex, each request is intercepted, the header is read  if URI is secured</p><br>
 * Created by SeanCunniffe on 26/Feb/2022
 */
@Provider
public class AuthFilter implements ContainerRequestFilter {


    @Inject
    private JwtUtil jwtUtil;

    EnumMap<ROLE, Map<String, List<Method>>> mappings;

    @PostConstruct
    void init() {
        mappings = new EnumMap<>(ROLE.class);
        Map<String, List<Method>> staffMapping = getStaffMapping();
        mappings.put(ROLE.STAFF, staffMapping);

        Map<String, List<Method>> customerMapping = getCustomerMapping();
        mappings.put(ROLE.CUSTOMER, customerMapping);

        Map<String, List<Method>> noRoleMapping = getNoRoleMapping();
        mappings.put(ROLE.EMPTY, noRoleMapping);
    }

    private Map<String, List<Method>> getNoRoleMapping() {
        final String authPath = "^\\/auth$",
                uniqueEmail = "^\\/users\\/unique-email",
                createUser = "^\\/users$",
                getControllers="\\/controller";
        Map<String, List<Method>> adminMapping = new HashMap<>();
        adminMapping.put(authPath, Arrays.asList(PUT, POST));
        adminMapping.put(uniqueEmail, Collections.singletonList(GET));
        adminMapping.put(createUser, Collections.singletonList(POST));
        adminMapping.put(getControllers, Collections.singletonList(GET));
        return adminMapping;
    }

    private Map<String, List<Method>> getStaffMapping() {
        final String userPath = "^\\/users",
                orderPath = "^\\/order";
        Map<String, List<Method>> staffMapping = new HashMap<>();
        staffMapping.put(userPath, Arrays.asList(GET, DELETE, PUT, POST));
        staffMapping.put(orderPath, Arrays.asList(GET, DELETE, PUT, POST));
        return staffMapping;
    }

    private HashMap<String, List<Method>> getCustomerMapping() {
        final String userWithIdOnly = "^\\/users\\/\\d+$",
                orderWithEmailOnly="^\\/order\\/search",
                orderPath = "^\\/order",
                deleteCustomerOrder = "^\\/order\\/\\d*$";
        HashMap<String, List<Method>> map = new HashMap<>();
        map.put(userWithIdOnly, Arrays.asList(GET));
        map.put(orderWithEmailOnly, Arrays.asList(GET));
        map.put(orderPath, Arrays.asList(POST));
        map.put(deleteCustomerOrder, Arrays.asList(DELETE, PUT));
        return map;
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String uriContext = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        List<ROLE> authRoles = getAuthRoles(uriContext, method);
        if (authRoles.contains(ROLE.EMPTY)) {
            return;
        }


        ROLE tokenRole = null;
        String token = null;
        try {
            List<String> authorization = requestContext.getHeaders().get("authorization");
            token = authorization.get(0);

            token = token.replace("Bearer ", "");
            boolean isAccessToken = jwtUtil.validateToken(token, TokenType.ACCESS);
            if (!isAccessToken)
                abort(403, "Not access token");


            tokenRole = jwtUtil.getRoleFromToken(token);
            if (!authRoles.contains(tokenRole))
                abort(403, "Unauthorized user type");

        } catch (NullPointerException e) {
            e.printStackTrace();
            abort(401, "no authentication header");
        } catch (Exception e) {
            abort(401, e.getMessage());
        }

        requestContext.setSecurityContext(getSecurityContext(jwtUtil.getEmailFromToken(token), tokenRole));
    }

    SecurityContext getSecurityContext(String emailFromToken, ROLE tokenRole) {
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> emailFromToken;
            }

            @Override
            public boolean isUserInRole(String role) {
                return tokenRole == ROLE.valueOf(role);
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return "JWT";
            }
        };
    }


    /**
     * <p>Gets the roles from the regex uris for the given URI and method
     * First checks the if the regex doesnt require authentication
     * and returns early with empty array </p>
     *
     * @param uri     uri being accessed
     * @param methodS http method in string form
     * @return A list of roles allowed to access this endpoint
     */
    public List<ROLE> getAuthRoles(String uri, String methodS) {
        Method method = Method.valueOf(methodS);
        List<ROLE> allowedRole = new ArrayList<>();

        BiConsumer<Map.Entry<String, List<Method>>, ROLE> roles = (entry, role) -> {
            String regex = entry.getKey();
            List<Method> methods = entry.getValue();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find() && methods.contains(method))
                allowedRole.add(role);
        };

        mappings.get(ROLE.EMPTY).entrySet().forEach(entry -> roles.accept(entry, ROLE.EMPTY));
        boolean allowAll = !allowedRole.isEmpty();
        if (allowAll)
            return allowedRole;

        mappings.get(ROLE.CUSTOMER).entrySet().forEach(entry -> roles.accept(entry, ROLE.CUSTOMER));

        mappings.get(ROLE.STAFF).entrySet().forEach(entry -> roles.accept(entry, ROLE.STAFF));

        return allowedRole;
    }


    /**
     * Abort invoking endpoint with status and message
     *
     * @param status  status code to return in response
     * @param message message to return in response, nothing added if null
     */
    public void abort(int status, String message) {
        throw new WebApplicationException(message, status);
    }

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    enum Method {
        GET, POST, DELETE, PUT
    }
}


