package duke;

import java.io.FileNotFoundException;
import java.util.Scanner;

import duke.command.Command;
import duke.exception.DukeException;
import duke.task.TaskList;

public class Duke {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;
    
    public Duke(String pathString) {
        ui = new Ui();
        storage = new Storage(pathString);
        ui.showIsLoading();
        try {
            tasks = new TaskList(storage.load());
            ui.showLoadingSuccess();
        } catch (FileNotFoundException fnfe) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                ui.showPrompt();
                String fullCommand = scanner.nextLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (DukeException de) {
                ui.showError(de.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        Duke duke = new Duke("storage/tasks.txt");
        duke.start();
    }
}
