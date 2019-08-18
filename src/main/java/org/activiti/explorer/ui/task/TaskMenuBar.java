package org.activiti.explorer.ui.task;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import java.util.List;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.identity.LoggedInUser;
import org.activiti.explorer.ui.Images;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
import org.activiti.explorer.ui.custom.ToolbarPopupEntry;
import org.activiti.explorer.ui.task.data.ArchivedListQuery;
import org.activiti.explorer.ui.task.data.InboxListQuery;
import org.activiti.explorer.ui.task.data.InvolvedListQuery;
import org.activiti.explorer.ui.task.data.QueuedListQuery;
import org.activiti.explorer.ui.task.data.TasksListQuery;

public class TaskMenuBar extends ToolBar {
	private static final long serialVersionUID = 1L;
	public static final String ENTRY_TASKS = "tasks";
	public static final String ENTRY_INBOX = "inbox";
	public static final String ENTRY_QUEUED = "queued";
	public static final String ENTRY_INVOLVED = "involved";
	public static final String ENTRY_ARCHIVED = "archived";
	protected transient IdentityService identityService;
	protected ViewManager viewManager;
	protected I18nManager i18nManager;

	public TaskMenuBar() {
		this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
		this.viewManager = ExplorerApp.get().getViewManager();
		this.i18nManager = ExplorerApp.get().getI18nManager();

		initItems();
		initActions();
	}

	protected void initItems() {
		setWidth("100%");

		long inboxCount = new InboxListQuery().size();
		ToolbarEntry inboxEntry = addToolbarEntry("inbox", this.i18nManager.getMessage("task.menu.inbox"),
				new ToolbarEntry.ToolbarCommand() {
					public void toolBarItemSelected() {
						TaskMenuBar.this.viewManager.showInboxPage();
					}
				});
		inboxEntry.setCount(Long.valueOf(inboxCount));

		LoggedInUser user = ExplorerApp.get().getLoggedInUser();
		long tasksCount = new TasksListQuery().size();
		ToolbarEntry tasksEntry = addToolbarEntry("tasks", this.i18nManager.getMessage("task.menu.tasks"),
				new ToolbarEntry.ToolbarCommand() {
					public void toolBarItemSelected() {
						TaskMenuBar.this.viewManager.showTasksPage();
					}
				});
		tasksEntry.setCount(Long.valueOf(tasksCount));

		List<Group> groups = user.getGroups();
		ToolbarPopupEntry queuedItem = addPopupEntry("queued", this.i18nManager.getMessage("task.menu.queued"));
		long queuedCount = 0L;
		for (final Group group : groups) {
			long groupCount = new QueuedListQuery(group.getId()).size();

			queuedItem.addMenuItem(group.getName() + " (" + groupCount + ")", new ToolbarEntry.ToolbarCommand() {
				public void toolBarItemSelected() { //点击菜单，回调
					TaskMenuBar.this.viewManager.showQueuedPage(group.getId());
				} //角色名字(任务数量)

			});
			queuedCount += groupCount;
		}
		queuedItem.setCount(Long.valueOf(queuedCount));

		long involvedCount = new InvolvedListQuery().size();
		ToolbarEntry involvedEntry = addToolbarEntry("involved", this.i18nManager.getMessage("task.menu.involved"),
				new ToolbarEntry.ToolbarCommand() {
					public void toolBarItemSelected() {
						TaskMenuBar.this.viewManager.showInvolvedPage();
					}
				});
		involvedEntry.setCount(Long.valueOf(involvedCount));

		long archivedCount = new ArchivedListQuery().size();
		ToolbarEntry archivedEntry = addToolbarEntry("archived", this.i18nManager.getMessage("task.menu.archived"),
				new ToolbarEntry.ToolbarCommand() {
					public void toolBarItemSelected() {
						TaskMenuBar.this.viewManager.showArchivedPage();
					}
				});
		archivedEntry.setCount(Long.valueOf(archivedCount));
	}

	protected void initActions() {
		Button newCaseButton = new Button();
		newCaseButton.setCaption(this.i18nManager.getMessage("task.new"));
		newCaseButton.setIcon(Images.TASK_16);
		addButton(newCaseButton);

		newCaseButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				NewCasePopupWindow newTaskPopupWindow = new NewCasePopupWindow();
				TaskMenuBar.this.viewManager.showPopupWindow(newTaskPopupWindow);
			}
		});
	}
}
