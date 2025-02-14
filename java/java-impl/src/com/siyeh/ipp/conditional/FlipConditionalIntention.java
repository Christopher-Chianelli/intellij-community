/*
 * Copyright 2003-2022 Dave Griffith, Bas Leijdekkers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siyeh.ipp.conditional;

import com.intellij.codeInspection.util.IntentionName;
import com.intellij.psi.PsiConditionalExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.siyeh.IntentionPowerPackBundle;
import com.siyeh.ig.PsiReplacementUtil;
import com.siyeh.ig.psiutils.BoolUtils;
import com.siyeh.ig.psiutils.CommentTracker;
import com.siyeh.ipp.base.MCIntention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NotNull;

public final class FlipConditionalIntention extends MCIntention {

  @Override
  public @NotNull String getFamilyName() {
    return IntentionPowerPackBundle.message("flip.conditional.intention.family.name");
  }

  @Override
  public @IntentionName @NotNull String getTextForElement(@NotNull PsiElement element) {
    return IntentionPowerPackBundle.message("flip.conditional.intention.name");
  }

  @Override
  @NotNull
  public PsiElementPredicate getElementPredicate() {
    return new FlipConditionalPredicate();
  }

  @Override
  public void invoke(@NotNull PsiElement element) {
    final PsiConditionalExpression exp = (PsiConditionalExpression)element;

    final PsiExpression condition = exp.getCondition();
    final PsiExpression elseExpression = exp.getElseExpression();
    final PsiExpression thenExpression = exp.getThenExpression();
    assert elseExpression != null;
    assert thenExpression != null;
    CommentTracker tracker = new CommentTracker();
    final String newExpression = BoolUtils.getNegatedExpressionText(condition, tracker) + '?' +
                                 tracker.text(elseExpression) + ':' + tracker.text(thenExpression);
    PsiReplacementUtil.replaceExpression(exp, newExpression, tracker);
  }
}
