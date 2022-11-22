package eu.org.yexiaoye.passwordgeneration.exception;

/**
 * 当生成密码而没有字符规则传入时抛出
 */
public class CharacterRuleException extends RuntimeException{
    public CharacterRuleException() {
        super();
    }
    public CharacterRuleException(String message) {
        super(message);
    }
}
