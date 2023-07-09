/**
 *
 */
package org.quiteoldorange.i3textutils.bsl.parser;

import org.quiteoldorange.i3textutils.bsl.lexer.Lexer;

/**
 * @author ozolotarev
 *
 */
public class AnnotationNode
    extends AbsractBSLElementNode
{

    /**
     * @param stream
     */
    public AnnotationNode(Lexer stream)
    {
        super(stream);

        // &НаКлиенте
        // &НаСервере
        // &НаКлиентеНаСервереБезКонтекста
        // &НаСервереБезКонтекста

        // &Перед("Метод")
        // &После("Метод")
        // &Вместо("Метод")

    }

}
