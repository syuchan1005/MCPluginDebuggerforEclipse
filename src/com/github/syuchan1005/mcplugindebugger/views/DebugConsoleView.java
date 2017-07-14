package com.github.syuchan1005.mcplugindebugger.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.github.syuchan1005.mcplugindebugger.PluginDataConfig;
import com.github.syuchan1005.mcplugindebugger.ServerProcessManager;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;

public class DebugConsoleView extends Composite {
	private Display display;
	private Composite parent;
	private Text serverJarFile;
	private Button serverBrowseButton;
	private Text pluginJarFile;
	private Button pluginBrowseButton;
	private Text hostField;
	private Button hostSetButton;
	private Text connectPort;
	private Button portSetButton;
	private Text pluginName;
	private Button pluginNameSetButton;
	private Button reloadPluginButton;
	private Button startButton;
	private Button stopButton;
	private Button reloadButton;
	private Button rebootButton;
	private Button forceStopButton;
	private Text outTextArea;
	private Text commandField;
	private Button sendCommandButton;
	
	private PluginDataConfig config = new PluginDataConfig();
	private ServerProcessManager processManager;

	public DebugConsoleView(Composite parent, int style) {
		super(parent, style);
		this.display = parent.getShell().getDisplay();
		this.parent = parent;
		setLayout(new GridLayout(6, false));
		
		Label lblServerjarfile = new Label(this, SWT.NONE);
		lblServerjarfile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerjarfile.setText("ServerJarFile");
		
		serverJarFile = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		serverJarFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		serverBrowseButton = new Button(this, SWT.NONE);
		serverBrowseButton.setText("Browse");
		
		Label lblPluginjarfile = new Label(this, SWT.NONE);
		lblPluginjarfile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPluginjarfile.setText("PluginJarFile");
		
		pluginJarFile = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		pluginJarFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		pluginBrowseButton = new Button(this, SWT.NONE);
		pluginBrowseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		pluginBrowseButton.setText("Browse");
		
		Label lblHost = new Label(this, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host");
		
		hostField = new Text(this, SWT.BORDER);
		hostField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		hostSetButton = new Button(this, SWT.NONE);
		hostSetButton.setText("Set");
		
		Label lblHost_1 = new Label(this, SWT.NONE);
		lblHost_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost_1.setText("Port");
		
		connectPort = new Text(this, SWT.BORDER);
		connectPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		portSetButton = new Button(this, SWT.NONE);
		portSetButton.setText("Set");
		
		Label lblPluginname = new Label(this, SWT.NONE);
		lblPluginname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPluginname.setText("PluginName");
		
		pluginName = new Text(this, SWT.BORDER);
		pluginName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		pluginNameSetButton = new Button(this, SWT.NONE);
		pluginNameSetButton.setText("Set");

		reloadPluginButton = new Button(this, SWT.NONE);
		reloadPluginButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		reloadPluginButton.setText("ReloadPlugin");
		
		Composite composite = new Composite(this, SWT.NONE);
		FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
		fl_composite.spacing = 5;
		composite.setLayout(fl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		
		Label lblServer = new Label(composite, SWT.NONE);
		lblServer.setAlignment(SWT.CENTER);
		lblServer.setText("Server");
		
		startButton = new Button(composite, SWT.NONE);
		startButton.setText("Start");
		
		stopButton = new Button(composite, SWT.NONE);
		stopButton.setText("Stop");
		
		reloadButton = new Button(composite, SWT.NONE);
		reloadButton.setText("Reload");
		
		rebootButton = new Button(composite, SWT.NONE);
		rebootButton.setText("Reboot");
		
		forceStopButton = new Button(composite, SWT.NONE);
		forceStopButton.setText("ForceStop");
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 6, 1));
		composite_1.setBackgroundMode(SWT.INHERIT_FORCE);
		
		outTextArea = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		outTextArea.setText("");
		
		commandField = new Text(this, SWT.BORDER);
		commandField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		sendCommandButton = new Button(this, SWT.NONE);
		sendCommandButton.setText("SendCommand");
		
		this.config.init(new File(""));
		processManager = new ServerProcessManager(this.config, line -> {
			display.asyncExec(() -> {
				if (line.equals("STX")) outTextArea.setText("");
				else outTextArea.append(line + "\n");
			});
		});
				
		setValues();
		setActions();
	}

	@Override
	protected void checkSubclass() {}
	
	private void setValues() {
		serverJarFile.setText(config.getServerJarFile().getAbsolutePath());
		pluginJarFile.setText(config.getPluginJarFile().getAbsolutePath());
		pluginName.setText(config.getPluginName());
		hostField.setText(config.getHost());
		connectPort.setText(config.getPort().toString());
	}
	
	private void setActions() {
		serverBrowseButton.addListener(SWT.Selection, (e) -> {
			FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
			try {
				String path = dialog.open();
				if (path == null) return;
				config.setServerJarFile(new File(path));
	            this.serverJarFile.setText(config.getServerJarFile().getAbsolutePath());
				
			} catch (Exception ignored) {}
		});
		pluginBrowseButton.addListener(SWT.Selection, (e) -> {
			FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
			try {
				String path = dialog.open();
				if (path == null) return;
				config.setPluginJarFile(new File(path));
	            this.serverJarFile.setText(config.getPluginJarFile().getAbsolutePath());
				
			} catch (Exception ignored) {}
		});

		pluginNameSetButton.addListener(SWT.Selection, (e) -> {
			config.setPluginName(pluginName.getText());
		});
		hostSetButton.addListener(SWT.Selection, (e) -> {
			config.setHost(hostField.getText());
		});
		portSetButton.addListener(SWT.Selection, (e) -> {
			config.setPort(Integer.valueOf(connectPort.getText()));
		});

		reloadPluginButton.addListener(SWT.Selection, (e) -> {
			new Thread(() -> {
				reloadPluginButton.setEnabled(false);
				try (Socket socket = new Socket(config.getHost(), config.getPort());
					 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
					dataOutputStream.writeUTF(config.getPluginName());
					if (!socket.isClosed()) {
						FileInputStream fileInputStream = new FileInputStream(config.getPluginJarFile());
						byte[] buffer = new byte[512];
						int fLength;
						while ((fLength = fileInputStream.read(buffer)) > 0) {
							dataOutputStream.write(buffer, 0, fLength);
						}
						fileInputStream.close();
					}
					dataOutputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					reloadPluginButton.setEnabled(true);
				}
			}).start();
		});

		// ServerButtons
		startButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.startServer();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		stopButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.stopServer();
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}
		});
		reloadButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.reload();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		rebootButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.reboot();
			} catch (InterruptedException | IOException e1) {
				e1.printStackTrace();
			}
		});
		forceStopButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.forceStop();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		commandField.addListener(SWT.Traverse, (e) -> {
			if(e.detail == SWT.TRAVERSE_RETURN) {
				sendCommandButton.notifyListeners(SWT.Selection, new Event());
            }
		});
		sendCommandButton.addListener(SWT.Selection, (e) -> {
			try {
				processManager.writeCommand(commandField.getText() + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	
}
