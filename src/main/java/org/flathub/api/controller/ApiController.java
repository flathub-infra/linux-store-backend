package org.flathub.api.controller;

import org.flathub.api.model.App;
import org.flathub.api.service.ApiService;

import org.freedesktop.appstream.AppdataParser;
import org.freedesktop.appstream.ComponentParser;
import org.freedesktop.appstream.appdata.Component;
import org.freedesktop.appstream.appdata.Components;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@RestController
@RequestMapping("/hello")
public class ApiController {

    @Autowired
    private ApiService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<App> findAll() {

        List<App> list = service.findAllApps();

        //objectToXML();
        //xmlToObject();
        //importGnomeAppData();

        return list;
    }

    private void importGnomeAppData() {

        File file = new File("/home/jorge/IdeaProjects/Flathub/appstream-appdata/src/test/resources/appstream-gnome-apps.xml");

        try {
            App app;
            Components components = AppdataParser.parseFile(file);

            for (Component component: components.getComponent()) {

                app = new App();

                System.out.println("--------------------------");
                System.out.println("Id:" + ComponentParser.getId(component));
                System.out.println("FlatpakId:" + ComponentParser.getFlatpakId(component));

                app.setName(ComponentParser.getName(component));
                app.setSummary(ComponentParser.getSummary(component));
                app.setDescription(ComponentParser.getDescription(component));

                //TODO:
                //Categories
                //Icons
                //Keywords (translatable)
                //kudos
                //Screenshots
                //Languages (percentage)
                //<bundle type="flatpak" runtime="org.gnome.Platform/x86_64/3.22" sdk="org.gnome.Sdk/x86_64/3.22">app/org.gnome.Weather/x86_64/stable</bundle>

                service.addApp(app);

            }


        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

   /* private void xmlToObject() {

        File file = new File("/home/jorge/IdeaProjects/Flathub/api/src/main/resources/appstream/appstream-example.xml");
        App app;
        String appFlatpakId = "";
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Components.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Components components = (Components) jaxbUnmarshaller.unmarshal(file);
            System.out.println(components);


            for (Component c : components.getComponent()) {
                app = new App();

                if(!"".equalsIgnoreCase(c.getBundle().getValue())){
                    String[] idArray = c.getBundle().getValue().split("/");
                    if(idArray.length == 4 && idArray[1] != null){
                        appFlatpakId = idArray[1];
                        //app.setAppid();

                        if(c.getName() != null && c.getName().size()> 0 && c.getName().get(0) != null){
                            app.setName(c.getName().get(0).getValue());
                        }
                        if(c.getSummary() != null && c.getSummary().size()> 0 && c.getSummary().get(0) != null){
                            app.setSummary(c.getSummary().get(0).getValue());
                        }
//                        if(c.getDescription() != null && c.getDescription().size()> 0 && c.getDescription().get(0) != null){
//                            app.setDescription(c.getDescription().get(0).getP()..get(0).get..get.getValue());
//                        }

                        if(c.getProjectLicense()!= null){
                            app.setProjectLicense(c.getProjectLicense());
                        }

                        for (Url url : c.getUrl())
                        {
                            switch (url.getType()) {
                                case "homepage":
                                    app.setHomepageUrl(url.getValue());
                                    break;
                                case "bugtracker":
                                    app.setBugtrackerUrl(url.getValue());
                                    break;
                                case "donation":
                                    break;
                                case "help":
                                    break;
                                default:
                                    break;
                            }
                        }


                        service.addApp(app);

                        System.out.println("FlatpakId:" + appFlatpakId + " id:" + c.getId() + " name:" + app.getName());

                    }
                }

            }

        } catch (JAXBException e1) {
            e1.printStackTrace();
        }

    }


    private void objectToXML() {

        File file = new File("/home/jorge/IdeaProjects/Flathub/api/src/main/resources/appstream/calendar-out.xml");

        Components components = new Components();

        components.setOrigin("jorge");
        components.setVersion("1");
        Component myApp = new Component();
        myApp.setId("Hola");

        components.getComponent().add(myApp);


        try {


            JAXBContext jaxbContext = JAXBContext.newInstance(Components.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(components, file);
            jaxbMarshaller.marshal(components, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }
*/

}
