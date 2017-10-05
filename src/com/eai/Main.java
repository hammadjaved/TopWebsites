package com.eai;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/* Here are a few assumptions that I am making:
1. The structure of the website with element positions and class names used in the html document will not change.
2. The number of characters in the HTML document will not exceed Integer.MAX_VALUE.
3. The total number of websites mentioned will not exceed 500. (This is because I am loading
   all the data for websites at once if the number were to exceed I would load the data asynchronously
   but for not I don't think that is necessary.)
*/
public class Main {
    private static final String url = "http://topsites.eaiti.com/";

    //Returns the total Number of Pages from the website.
    private static int getTotalPages(String initialData) {
        String[] maxPages = initialData.split("page=");
        String last = maxPages[maxPages.length - 1];
        return Integer.parseInt(last.substring(0, last.indexOf("\"")));
    }

    //Returns a String with the HTML of the url specified.
    private static String getStringFromURL(String url) throws IOException {
        try {
            return URLToHTMLConverter.download(new URL(url));
        } catch (IOException e) {
            System.out.print("The website to access the top sites cannot be reached, please try to run the program at a later time. ");
            System.exit(0);
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        //Gets access to the first page and calculates the range of acceptable values of n.
        String pageData = getStringFromURL(url);

        //Gets a list of websites on the first page.
        String[] websites = pageData.split("class=\"number\"");

        int pages = getTotalPages(pageData);
        int perPageShown = websites.length - 1;

        //Get the maxValue of N that is possible.
        int maxValue = (pages + 1) * perPageShown;


        //Get N from command line
        System.out.print("Please enter the number of top websites that you want to display in the range ( 1 - " + maxValue +" ) : \n");
        int n;
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                n = sc.nextInt();

                if (n < 1 || n > 500) { //Check if value of n is in the valid range of minValue to maxValue
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) { //Checks if an invalid input type is entered.
                System.out.print("Please enter a valid value for N between the range " + 1 + " - " + maxValue + "\n");
            }
        }

        //Determine how many pages need to be loaded in order to get output n websites.
        int pagesToLoad = (n / perPageShown);
        if(n % perPageShown !=0){
            pagesToLoad++;
        }

        //Add the websites from the first page because they have already been loaded.
        ArrayList<String> allWebsites = new ArrayList<>();
        Collections.addAll(allWebsites, Arrays.copyOfRange(websites, 1, websites.length ));

        //Load and add the reminder of the websites.
        for (int i = 1; i < pagesToLoad; i++) {
            pageData = getStringFromURL(url + "?page=" + i);
            websites = pageData.split("class=\"number\"");
            Collections.addAll(allWebsites, Arrays.copyOfRange(websites, 1, websites.length ));

        }

        //Print all the websites up to the count of n.
        int count = 1;
        while(count <= n ){
                String curWebsite = allWebsites.get(count-1);

                //Extracts the website domain name out by removing information on either side.
                curWebsite = curWebsite.substring(curWebsite.indexOf("<a href=\"/site/"), curWebsite.indexOf("</a>"));
                curWebsite = curWebsite.substring(curWebsite.indexOf(">") + 1, curWebsite.length());

                System.out.print(count + ". " + curWebsite + "\n");
                count++;
        }

    }


}
