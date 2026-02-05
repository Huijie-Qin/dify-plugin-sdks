package com.dify.dify_plugin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 简化的国际化对象，仅保留英文文案。
 */
@Data
@AllArgsConstructor
public class I18nObject {
    /** 英文文案。 */
    private String enUs;
}
