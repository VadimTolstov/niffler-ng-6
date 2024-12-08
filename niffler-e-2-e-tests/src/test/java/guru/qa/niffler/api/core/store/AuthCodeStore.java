package guru.qa.niffler.api.core.store;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public enum AuthCodeStore {
    INSTANCE;

    private final static ThreadLocal<String> threadSafeStore = ThreadLocal.withInitial(String::new);

    @Nullable
    public String getCode() {
        return threadSafeStore.get();
    }

    public void setCode(String code) {
        threadSafeStore.set(code);
    }
}
