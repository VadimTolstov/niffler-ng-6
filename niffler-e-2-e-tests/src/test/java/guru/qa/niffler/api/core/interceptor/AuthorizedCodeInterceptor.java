package guru.qa.niffler.api.core.interceptor;

import guru.qa.niffler.api.core.store.AuthCodeStore;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@ParametersAreNonnullByDefault
public class AuthorizedCodeInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        final String code = chain.request().url().queryParameter("code");
        if (isNotEmpty(code)) {
            AuthCodeStore.INSTANCE.setCode(code);
        }
        return chain.proceed(chain.request());
    }
}
