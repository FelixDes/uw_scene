package com.uw.di;

import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Context implements Disposable {
    public static final Context instance = new Context();

    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Set<Disposable> disposables = new HashSet<>();

    public <T> T put(T instance) {
        if (instance instanceof Disposable disposable) {
            disposables.add(disposable);
        }
        singletons.put(instance.getClass(), instance);
        return instance;
    }

    private Context() {
    }

    public <T> T get(Class<T> clazz) {
        return (T) singletons.get(clazz);
    }

    @Override
    public void dispose() {
        disposables.forEach(Disposable::dispose);
    }
}
