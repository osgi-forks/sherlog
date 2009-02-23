package org.javakontor.sherlog.ui.loadwizzard;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.javakontor.sherlog.core.reader.LogEventFlavour;
import org.javakontor.sherlog.core.reader.LogEventReader;
import org.javakontor.sherlog.core.reader.LogEventReaderFactory;
import org.lumberjack.application.request.RequestHandlerImpl;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

// TODO: MVC
public class LoadLogFileWizzard extends JDialog {

  private final LogEventReaderFactory _logEventReaderFactory;

  private JButton                     _okButton;

  private JButton                     _cancelButton;

  private LogFileChooserModel         _logFileChooserModel;

  /** The selected {@link LogEventReader} or null if cancel was pressed */
  private LogEventReader              _logEventReader;

  /**
   * 
   */
  private static final long           serialVersionUID = 1L;

  public static LogEventReader openLogFileDialog(Window owner, LogEventReaderFactory logEventReaderFactory) {
    LoadLogFileWizzard dialog;
    if (owner instanceof JFrame) {
      dialog = new LoadLogFileWizzard((JFrame) owner, logEventReaderFactory);
    } else {
      dialog = new LoadLogFileWizzard((JDialog) owner, logEventReaderFactory);
    }
    dialog.setVisible(true);
    return dialog.getLogEventReader();
  }

  public LoadLogFileWizzard(JFrame owner, LogEventReaderFactory logEventReaderFactory) {
    super(owner);
    this._logEventReaderFactory = logEventReaderFactory;
    setUp();
  }

  public LoadLogFileWizzard(JDialog dialog, LogEventReaderFactory logEventReaderFactory) {
    super(dialog);
    this._logEventReaderFactory = logEventReaderFactory;
    setUp();
  }

  protected void setUp() {
    setTitle(LoadLogFileWizzardMessages.loadLogFile);
    setModal(true);

    this._logFileChooserModel = new LogFileChooserModel(this._logEventReaderFactory);
    LogFileChooserView view = new LogFileChooserView(this._logFileChooserModel);
    new LogFileChooserController(this._logFileChooserModel, view, new LogFileOpenDialogHandler());

    JPanel contentPanel = new JPanel();
    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    FormLayout layout = new FormLayout("fill:pref:grow", "fill:pref:grow,15dlu,bottom:min,5dlu,bottom:pref");
    contentPanel.setLayout(layout);

    // ~~ build the buttons and the button bar
    this._okButton = new JButton("OK");
    // this._okButton.setEnabled(this._logFileChooserModel.isFormValid());
    this._cancelButton = new JButton("Cancel");

    PanelBuilder builder = new PanelBuilder(layout, contentPanel);
    CellConstraints cc = new CellConstraints();
    builder.add(view, cc.rc(1, 1));
    builder.addSeparator("", cc.rc(3, 1));
    builder.add(ButtonBarFactory.buildRightAlignedBar(this._okButton, this._cancelButton), cc.rc(5, 1));

    createListener();

    add(contentPanel);

    // layout components
    pack();

    // position dialog in the center of it's owning window
    setLocationRelativeTo(getOwner());
  }

  protected void createListener() {

    this._cancelButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        LoadLogFileWizzard.this._logEventReader = null;
        setVisible(false);
      }
    });

    this._okButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        ValidationResult result = _logFileChooserModel.validateForm();
        if (result.hasError()) {
          JOptionPane.showMessageDialog(LoadLogFileWizzard.this, result.getErrorMessage(),
              LoadLogFileWizzardMessages.couldNotLoadLogFile, JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          URL url = LoadLogFileWizzard.this._logFileChooserModel.getSelectedLogFile().toURL();
          LogEventFlavour logEventFlavour = LoadLogFileWizzard.this._logFileChooserModel.getSelectedFlavour();

          LoadLogFileWizzard.this._logEventReader = LoadLogFileWizzard.this._logEventReaderFactory.getLogEventReader(
              url, logEventFlavour);

          setVisible(false);
        } catch (MalformedURLException ex) {
          ex.printStackTrace();
        }
      }
    });

    // this._logFileChooserModel
    // .addModelChangedListener(new ModelChangedListener<LogFileChooserModel, DefaultReasonForChange>() {
    //
    // public void modelChanged(ModelChangedEvent<LogFileChooserModel, DefaultReasonForChange> event) {
    // LogFileOpenDialog.this._okButton.setEnabled(LogFileOpenDialog.this._logFileChooserModel.isFormValid());
    // }
    // });
    //
  }

  class LogFileOpenDialogHandler extends RequestHandlerImpl {

  }

  public LogEventReader getLogEventReader() {
    return this._logEventReader;
  }

}
