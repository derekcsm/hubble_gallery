package com.derek_s.hubble_gallery.utils;

import com.derek_s.hubble_gallery._shared.model.SectionChildObject;
import com.derek_s.hubble_gallery.nav_drawer.model.SectionObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dereksmith on 15-03-05.
 */
public class NavDataUtils {

    public static ArrayList<SectionObject> addAllGroups() {
        ArrayList<SectionObject> result = new ArrayList<>();

        SectionObject section = new SectionObject();
        section.setSectionTitle("Entire Collection");
        section.setQuery("entire");
        section.setExpandable(false);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Hubble Heritage");
        section.setQuery("heritage");
        section.setExpandable(false);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("The Universe");
        section.setQuery("the_universe");
        section.setExpandable(true);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Exotic");
        section.setQuery("exotic");
        section.setExpandable(true);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Galaxies");
        section.setQuery("galaxy");
        section.setExpandable(true);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Nebulae");
        section.setQuery("nebula");
        section.setExpandable(true);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Solar System");
        section.setQuery("solar_system");
        section.setExpandable(true);
        result.add(section);

        section = new SectionObject();
        section.setSectionTitle("Stars");
        section.setQuery("star");
        section.setExpandable(true);
        result.add(section);

        return result;
    }

    public static HashMap<String, ArrayList<SectionChildObject>> getSectionChildren() {
        HashMap<String, ArrayList<SectionChildObject>> result = new HashMap<>();

        ArrayList<SectionChildObject> subList = new ArrayList<>();
        SectionChildObject section = new SectionChildObject();

        /*
        the_universe
         */

        section.setSectionTitle("Distant Galaxies");
        section.setQuery("the_universe/distant_galaxies");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Intergalactic Gas");
        section.setQuery("the_universe/intergalactic_gas");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("GOODS");
        section.setQuery("the_universe/goods");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Hubble Deep Field");
        section.setQuery("the_universe/hubble_deep_field");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Hubble Ultra Deep Field");
        section.setQuery("the_universe/hubble_ultra_deep_field");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Medium Deep Survey");
        section.setQuery("the_universe/medium_deep_survey");
        subList.add(section);

        result.put("the_universe", subList);

        /*
        exotic
        */

        subList = new ArrayList<>();
        section = new SectionChildObject();
        section.setSectionTitle("Black Hole");
        section.setQuery("exotic/black_hole");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Dark Matter");
        section.setQuery("exotic/dark_matter");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Gamma Ray Burst");
        section.setQuery("exotic/gamma_ray_burst");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Gravitational Lens");
        section.setQuery("exotic/gravitational_lens");
        subList.add(section);

        result.put("exotic", subList);

        /*
        galaxy
        */

        subList = new ArrayList<>();
        section = new SectionChildObject();
        section.setSectionTitle("Cluster");
        section.setQuery("galaxy/cluster");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Dwarf");
        section.setQuery("galaxy/dwarf");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Elliptical");
        section.setQuery("galaxy/elliptical");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Interacting");
        section.setQuery("galaxy/interacting");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Cluster");
        section.setQuery("galaxy/cluster");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Irregular");
        section.setQuery("galaxy/irregular");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Magellanic Cloud");
        section.setQuery("galaxy/magellanic_cloud");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Quasar/Active Nucleus");
        section.setQuery("galaxy/quasar_active_nucleus");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Spiral");
        section.setQuery("galaxy/spiral");
        subList.add(section);

        result.put("galaxy", subList);

        /*
        nebula
        */

        subList = new ArrayList<>();
        section = new SectionChildObject();
        section.setSectionTitle("Dark");
        section.setQuery("nebula/dark");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Emission");
        section.setQuery("nebula/emission");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Planetary");
        section.setQuery("nebula/planetary");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Reflection");
        section.setQuery("nebula/reflection");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Supernova Remnant");
        section.setQuery("nebula/supernova_remnant");
        subList.add(section);

        result.put("nebula", subList);

        /*
        solar_system
        */

        subList = new ArrayList<>();
        section = new SectionChildObject();
        section.setSectionTitle("Venus");
        section.setQuery("solar_system/venus");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Mars");
        section.setQuery("solar_system/mars");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Jupiter");
        section.setQuery("solar_system/jupiter");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Saturn");
        section.setQuery("solar_system/saturn");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Uranus");
        section.setQuery("solar_system/uranus");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Neptune");
        section.setQuery("solar_system/neptune");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Pluto");
        section.setQuery("solar_system/pluto");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Planetary Moon");
        section.setQuery("solar_system/planetary_moon");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Planetary Ring");
        section.setQuery("solar_system/planetary_ring");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Weather/Atmosphere");
        section.setQuery("solar_system/weather_atmosphere");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Asteroid");
        section.setQuery("solar_system/asteroid");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Comet");
        section.setQuery("solar_system/comet");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Kuiper Belt Object");
        section.setQuery("solar_system/kuiper_belt_object");
        subList.add(section);

        result.put("solar_system", subList);

        /*
        star
        */

        subList = new ArrayList<>();
        section = new SectionChildObject();
        section.setSectionTitle("Brown Dwarf");
        section.setQuery("star/brown_dwarf");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Massive Star");
        section.setQuery("star/massive_star");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Nova");
        section.setQuery("star/nova");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Protostellar Jet");
        section.setQuery("star/protostellar_jet");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Protoplanetary Disk");
        section.setQuery("star/protoplanetary_disk");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Pulsar");
        section.setQuery("star/pulsar");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Star Field");
        section.setQuery("star/star_field");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Supernova");
        section.setQuery("star/supernova");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Variable Star");
        section.setQuery("star/variable_star");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("White Dwarf");
        section.setQuery("star/white_dwarf");
        subList.add(section);

        section = new SectionChildObject();
        section.setSectionTitle("Star Cluster");
        section.setQuery("star/star_cluster");
        subList.add(section);

        result.put("star", subList);

        return result;
    }
}
