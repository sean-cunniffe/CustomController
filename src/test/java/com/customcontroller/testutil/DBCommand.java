
package com.customcontroller.testutil;
import org.junit.Ignore;

@Ignore
public interface DBCommand<T> {

	T execute();

}
