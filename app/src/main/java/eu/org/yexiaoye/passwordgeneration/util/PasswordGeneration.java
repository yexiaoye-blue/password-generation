package eu.org.yexiaoye.passwordgeneration.util;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

import eu.org.yexiaoye.passwordgeneration.exception.CharacterRuleException;

public class PasswordGeneration {

    public static final String ERROR_CODE = "CUSTOM_CHARACTER_ERROR";

    private PasswordGeneration() {}

    /**
     * 生成密码
     * 注意：
     *  密码的最小长度为6
     *  至少存在一种字符规则,否则将抛出异常：NoCharacterRuleException
     * @param passwordLength    密码长度
     * @param hasLowerCase      是否包含小写字符
     * @param hasUpperCase      是否包含大写字符
     * @param hasDigit          是否包含数字
     * @param hasSpecialChars   是否包含特殊字符()`!@#$%^&*_-+=|{}[]:;'<>,.?
     * @return                  生成的密码
     */
    public static String generatePassword(int passwordLength,boolean hasLowerCase,boolean hasUpperCase, boolean hasDigit, boolean hasSpecialChars) {
        // 自定义特殊字符数据
        CharacterData specialCharacters = new CharacterData() {
            @Override
            public String getErrorCode() {
                return ERROR_CODE;
            }

            @Override
            public String getCharacters() {
                return "()`!@#$%^&*_-+=|{}[]:;'<>,.?";
            }
        };

        // 如果一种规则都没有则抛出异常
        if (!(hasLowerCase || hasUpperCase || hasDigit || hasSpecialChars)) {
            throw new CharacterRuleException("请至少传入一种字符规则!");
        }
        List<CharacterRule> characterRuleList = new ArrayList<>();
        // 设置该字符在密码中至少出现的次数
        if (passwordLength >=6 && passwordLength <= 12) {
            // 密码长度为6~12的规则
            if (hasLowerCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.LowerCase,1));
            }

            if (hasUpperCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.UpperCase,1));
            }

            if (hasDigit) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.Digit,1));
            }

            if (hasSpecialChars) {
                characterRuleList.add(new CharacterRule(specialCharacters,1));
            }
        }else if (passwordLength >= 13 && passwordLength <= 18) {
            if (hasLowerCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.LowerCase,1));
            }

            if (hasUpperCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.UpperCase,2));
            }

            if (hasDigit) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.Digit,1));
            }

            if (hasSpecialChars) {
                characterRuleList.add(new CharacterRule(specialCharacters,2));
            }
        } else if (passwordLength >= 19 && passwordLength <= 32) {
            if (hasLowerCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.LowerCase,2));
            }

            if (hasUpperCase) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.UpperCase,2));
            }

            if (hasDigit) {
                characterRuleList.add(new CharacterRule(EnglishCharacterData.Digit,2));
            }

            if (hasSpecialChars) {
                characterRuleList.add(new CharacterRule(specialCharacters,2));
            }
        }else {
            throw new CharacterRuleException("密码长度不在约定范围！");
        }

        return new PasswordGenerator().generatePassword(passwordLength, characterRuleList);
    }
}
