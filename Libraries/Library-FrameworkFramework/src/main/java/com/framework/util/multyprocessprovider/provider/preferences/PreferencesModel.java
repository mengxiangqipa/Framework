package com.framework.util.multyprocessprovider.provider.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.framework.util.multyprocessprovider.provider.base.BaseModel;

/**
 * Data model for the {@code preferences} table.
 */
public interface PreferencesModel extends BaseModel {

    /**
     * Get the {@code module} value.
     * Can be {@code null}.
     *
     * @return module name.
     */
    @Nullable
    String getModule();

    /**
     * Get the {@code key} value.
     * Cannot be {@code null}.
     *
     * @return key string.
     */
    @NonNull
    String getKey();

    /**
     * Get the {@code value} value.
     * Can be {@code null}.
     *
     * @return value string.
     */
    @Nullable
    String getValue();
}
