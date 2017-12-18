import de.kotlincook.textmining.streetdivider.Location;
import de.kotlincook.textmining.streetdivider.StreetDivider;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Streetdivider");

        final StreetDivider streetDivider = new StreetDivider();
        final Location location = streetDivider.parse("1;2 23a");
        System.out.println(location);
    }

}
