package com.itcag.rockwell.tagger.lang;

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>This class represents a {@link com.itcag.rockwell.lang.Token token} or a {@link com.itcag.rockwell.lang.Semtoken semtoken} that was matched by a {@link com.itcag.rockwell.tagger.lang.ConditionElement condition element} of a Rockwell expression.</p>
 * <p>This class extends the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class by adding a Boolean property named <i>quodlibet</i> that specifies whether the match was within the Kleene closure. Infix tokens are selected as all quodlibet tokens between the edges. Infix is a type of the Rockwell {@link com.itcag.rockwell.tagger.lang.Affix affix}.</p>
 * <p>To learn more about infix and how Rockwell implements Kleene closure see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public final class Match extends Semtoken {

    private boolean quodlibet = false;
    
    /**
     * @param token Instance of the class {@link com.itcag.rockwell.lang.Token Token} or {@link com.itcag.rockwell.lang.Semtoken Semtoken} that was matched by a condition element of a Rockwell expression.
     */
    public Match(Token token) {
        super(token.getWord(), token.getPos(), token.getLemma(), token.getIndex(), new ArrayList<>(Arrays.asList(token)));
        if (token instanceof Semtoken) {
            Semtoken semtoken = (Semtoken) token;
            semtoken.getRoles().forEach((role) -> {
                super.addRole(role);
            });
        }
    }

    /**
     * @return Boolean value indicating whether the token was matched within a Kleene closure.
     */
    public boolean isQuodlibet() {
        return quodlibet;
    }

    /**
     * @param quodlibet Boolean value indicating whether the token was matched within a Kleene closure.
     */
    public void setQuodlibet(boolean quodlibet) {
        this.quodlibet = quodlibet;
    }

}
