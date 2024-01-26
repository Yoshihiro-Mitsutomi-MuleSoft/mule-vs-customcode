package com.example.americanflight.constraint;

/**
 * API仕様に記載のEnumのバリデーションを行うための列挙型
 * @ValueOfEnum および ValueOfEnumValidatorとともに使用
 * 
 */
public class DestinationConstraint {
    public enum Destinations {
        SFO,
        LAX,
        CLE
    }
}
