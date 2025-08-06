package it.simonetagliaferri.controller.graphic.cli;


import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.AddClubLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.AddClubCLIView;
import it.simonetagliaferri.view.cli.AddClubCLIView.PreambleChoice;
import static it.simonetagliaferri.view.cli.AddClubCLIView.PreambleChoice.ADD_CLUB;

public class GraphicAddClubControllerCLI extends GraphicController {

    AddClubCLIView view;
    AddClubLogicController controller;

    public GraphicAddClubControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new AddClubCLIView();
        this.controller = new AddClubLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getHostDAO());
    }

    public void start() {
        boolean result;
        PreambleChoice choice = view.preamble();
        if (choice.equals(ADD_CLUB)) {
            result = this.controller.addClub(addClub());
        }
        else return;
        if (result) {
            view.newClubAdded();
        }
        else {
            view.clubAlreadyExists();
        }
    }

    private ClubBean addClub() {
        ClubBean club = new ClubBean();
        club.setName(view.getClubName());
        club.setStreet(view.getClubStreet());
        club.setNumber(view.getClubNumber());
        club.setCity(view.getClubCity());
        club.setState(view.getClubState());
        club.setCountry(view.getClubCountry());
        club.setZip(view.getClubZip());
        club.setPhone(view.getClubPhone());
        return club;
    }
}
