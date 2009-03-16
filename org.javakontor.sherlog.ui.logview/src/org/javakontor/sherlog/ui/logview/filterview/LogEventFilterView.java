package org.javakontor.sherlog.ui.logview.filterview;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import org.javakontor.sherlog.core.filter.LogEventFilter;
import org.javakontor.sherlog.ui.filter.FilterConfigurationEditor;
import org.javakontor.sherlog.ui.filter.FilterConfigurationEditorFactory;
import org.lumberjack.application.mvc.AbstractView;
import org.lumberjack.application.mvc.ModelChangedEvent;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class LogEventFilterView extends AbstractView<LogEventFilterModel, LogEventFilterModelReasonForChange> {

  /** serialVersionUID */
  private static final long                              serialVersionUID = 1L;

  /** filterConfigurationEditors */
  private Map<LogEventFilter, FilterConfigurationEditor> _filterConfigurationEditors;

  /** filterConfigurationEditors */
  private Set<LogEventFilter>                            _unboundFilters;

  /**
   * <p>
   * Creates a new instance of type {@link LogEventFilterView}.
   * </p>
   * 
   * @param model
   *          the LogEventFilterModel
   */
  public LogEventFilterView(LogEventFilterModel model) {
    super(model);
  }

  @Override
  protected void setUp() {
    _filterConfigurationEditors = new HashMap<LogEventFilter, FilterConfigurationEditor>();
    _unboundFilters = new HashSet<LogEventFilter>();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  @Override
  public void onModelChanged(ModelChangedEvent<LogEventFilterModel, LogEventFilterModelReasonForChange> event) {

    switch (event.getReasonForChange()) {
    case filterAdded: {
      createEditor((LogEventFilter) event.getObjects()[0]);
      break;
    }
    case filterRemoved: {
      disposeEditor((LogEventFilter) event.getObjects()[0]);
      break;
    }
    case factoryAdded: {
      handleFactoryAdded((FilterConfigurationEditorFactory) event.getObjects()[0]);
      break;
    }
    case factoryRemoved: {
      removedFactory((FilterConfigurationEditorFactory) event.getObjects()[0]);
      break;
    }
    case factoryManagerAdded: {
      for (LogEventFilter logEventFilter : getModel().getLogEventFilter()) {
        createEditor(logEventFilter);
      }
      break;
    }
    case factoryManagerRemoved: {
      for (LogEventFilter logEventFilter : getModel().getLogEventFilter()) {
        disposeEditor(logEventFilter);
      }
      break;
    }
    default:
      // ignore
      break;
    }
  }

  private void handleFactoryAdded(FilterConfigurationEditorFactory filterConfigurationEditorFactory) {

    for (Iterator<LogEventFilter> iterator = _unboundFilters.iterator(); iterator.hasNext();) {
      LogEventFilter filter = iterator.next();
      if (createEditor(filter, filterConfigurationEditorFactory)) {
        iterator.remove();
      }
    }
  }

  private void removedFactory(FilterConfigurationEditorFactory filterConfigurationEditorFactory) {
    // TODO Auto-generated method stub

  }

  private void createEditor(LogEventFilter logEventFilter) {

    for (FilterConfigurationEditorFactory factory : getModel().getFilterConfigurationEditorFactories()) {

      if (createEditor(logEventFilter, factory)) {
        return;
      }
    }

    _unboundFilters.add(logEventFilter);
  }

  private boolean createEditor(final LogEventFilter logEventFilter, final FilterConfigurationEditorFactory factory) {

    if (factory.isSuitableFor(logEventFilter)) {
      // throw new RuntimeException("Bratz");
      System.err.println("createEditor " + logEventFilter);
      System.err.println("createEditor " + factory);
      FilterConfigurationEditor configurationEditor = factory.createFilterConfigurationEditor(logEventFilter);
      _filterConfigurationEditors.put(logEventFilter, configurationEditor);
      System.err.println("createEditor " + configurationEditor.getPanel());
      add(configurationEditor.getPanel());
      repaintComponent();

      return true;
    }
    return false;
  }

  private void disposeEditor(LogEventFilter logEventFilter) {
    System.err.println("disposeEditor");
    System.err.println(logEventFilter);

    FilterConfigurationEditor editor = _filterConfigurationEditors.remove(logEventFilter);
    if (editor != null) {
      remove(editor.getPanel());
      repaintComponent();
    }
    _unboundFilters.remove(logEventFilter);
  }

  /**
   *
   */
  private void repaintComponent() {
    revalidate();
    repaint();
  }
}
