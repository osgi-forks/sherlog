package org.javakontor.sherlog.ui.logview.filterview;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import org.javakontor.sherlog.core.filter.LogEventFilter;
import org.javakontor.sherlog.ui.filter.FilterConfigurationEditor;
import org.javakontor.sherlog.ui.filter.FilterConfigurationEditorFactory;
import org.javakontor.sherlog.ui.logview.osgi.GuiExecutor;
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
  private List<LogEventFilter>                           _unboundFilters;

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
    _unboundFilters = new LinkedList<LogEventFilter>();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    for (LogEventFilter logEventFilter : getModel().getLogEventFilter()) {
      createEditor(logEventFilter);
    }
  }

  public void modelChanged(ModelChangedEvent<LogEventFilterModel, LogEventFilterModelReasonForChange> event) {
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
    for (LogEventFilter filter : _unboundFilters) {
      if (createEditor(filter, filterConfigurationEditorFactory)) {
        _unboundFilters.remove(filter);
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
      GuiExecutor.execute(new Runnable() {
        public void run() {
          repaintComponent();
        }
      });
      ;

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
      GuiExecutor.execute(new Runnable() {
        public void run() {
          repaintComponent();
        }
      });

    }
  }

  /**
   * 
   */
  private void repaintComponent() {
    revalidate();
    repaint();
  }
}
