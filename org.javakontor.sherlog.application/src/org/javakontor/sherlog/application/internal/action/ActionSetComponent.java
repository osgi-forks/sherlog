package org.javakontor.sherlog.application.internal.action;

import java.util.Hashtable;
import java.util.Map;

import org.javakontor.sherlog.application.action.Action;
import org.javakontor.sherlog.application.action.ActionAdmin;
import org.javakontor.sherlog.application.action.ActionGroupType;
import org.javakontor.sherlog.application.action.contrib.ActionContribution;
import org.javakontor.sherlog.application.action.contrib.ActionGroupContribution;
import org.javakontor.sherlog.application.action.contrib.ActionGroupElementContribution;
import org.javakontor.sherlog.application.action.contrib.DefaultActionContribution;
import org.javakontor.sherlog.application.action.contrib.DefaultActionGroupContribution;
import org.javakontor.sherlog.application.action.set.ActionSet;
import org.javakontor.sherlog.application.action.set.ActionSetManager;

public class ActionSetComponent implements ActionSetManager, ActionAdmin {

  private final Map<String, ActionSet>                      _actionSets;

  private final Map<String, ActionGroupElementContribution> _actionGroupElementContributions;

  public ActionSetComponent() {
    this._actionSets = new Hashtable<String, ActionSet>();

    this._actionGroupElementContributions = new Hashtable<String, ActionGroupElementContribution>();
  }

  public void addAction(final String id, final String actionGroupId, final String label, final String shortcut,
      final Action action) {

    final DefaultActionContribution contribution = new DefaultActionContribution();
    contribution.setId(id);
    contribution.setTargetActionGroupId(actionGroupId);
    contribution.setLabel(label);
    contribution.setDefaultShortcut(shortcut);
    contribution.setAction(action);

    addActionContribution(contribution);
  }

  public void addActionGroup(final String id, final String actionGroupId, final String label,
      final ActionGroupType type, final ActionGroupContribution[] staticActionGroupIds,
      final ActionContribution[] staticActions) {

    final DefaultActionGroupContribution contribution = new DefaultActionGroupContribution();
    contribution.setId(id);
    contribution.setTargetActionGroupId(actionGroupId);
    contribution.setLabel(label);
    contribution.setType(type);

    addActionGroupContribution(contribution);
  }

  public void addActionGroup(final String id, final String actionGroupId, final String label, final ActionGroupType type) {
    addActionGroup(id, actionGroupId, label, type, null, null);
  }

  /**
   * @see org.javakontor.sherlog.application.action.ActionAdmin#removeAction(java.lang.String)
   */
  public void removeAction(final String id) {
    final ActionGroupElementContribution contribution = this._actionGroupElementContributions.get(id);

    if ((contribution != null) && (contribution instanceof ActionContribution)) {
      removeActionContribution((ActionContribution) contribution);
    }
  }

  /**
   * @see org.javakontor.sherlog.application.action.ActionAdmin#removeActionGroup(java.lang.String)
   */
  public void removeActionGroup(final String id) {
    final ActionGroupElementContribution contribution = this._actionGroupElementContributions.get(id);

    if ((contribution != null) && (contribution instanceof ActionGroupContribution)) {
      removeActionGroupContribution((ActionGroupContribution) contribution);
    }
  }

  public void addActionContribution(final ActionContribution action) {
    final ActionSetImpl registry = (ActionSetImpl) getActionSet(getActionSetId(action));
    registry.addAction(action);
    this._actionGroupElementContributions.put(action.getId(), action);
  }

  public void addActionGroupContribution(final ActionGroupContribution actionGroup) {
    final ActionSetImpl registry = (ActionSetImpl) getActionSet(getActionSetId(actionGroup));
    registry.addActionGroup(actionGroup);
    this._actionGroupElementContributions.put(actionGroup.getId(), actionGroup);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.javakontor.sherlog.application.action.ActionSetManager#getActionSet(java.lang.String)
   */
  public ActionSet getActionSet(final String actionSetId) {
    return getActionSet(actionSetId, true);
  }

  /**
   * @param rootId
   * @param createNew
   * @return
   */
  protected ActionSet getActionSet(final String rootId, final boolean createNew) {
    ActionSet actionSet;
    if (!this._actionSets.containsKey(rootId)) {
      actionSet = new ActionSetImpl(rootId);
      this._actionSets.put(rootId, actionSet);
    } else {
      actionSet = this._actionSets.get(rootId);
    }

    return actionSet;
  }

  protected String getActionSetId(final ActionGroupElementContribution actionGroupElement) {
    final String targetId = actionGroupElement.getTargetActionGroupId();
    final TargetGroupIdParser parser = new TargetGroupIdParser(targetId);
    return parser.getActionRoot();
  }

  public void removeActionContribution(final ActionContribution action) {
    final ActionSetImpl actionSet = (ActionSetImpl) getActionSet(getActionSetId(action), false);
    if (actionSet != null) {
      actionSet.removeAction(action);
    }
  }

  public void removeActionGroupContribution(final ActionGroupContribution actionGroup) {
    final ActionSetImpl actionSet = (ActionSetImpl) getActionSet(getActionSetId(actionGroup), false);
    if (actionSet != null) {
      actionSet.removeActionGroup(actionGroup);
    }
  }

}
