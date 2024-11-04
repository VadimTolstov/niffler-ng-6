package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.service.ThreadSafeCookiesStore;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CookieJarExtension implements AfterTestExecutionCallback {
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        ThreadSafeCookiesStore.INSTANCE.removeAll();
    }
}
