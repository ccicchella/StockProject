import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import javafx.application.Application; 
import javafx.scene.Group;
import javafx.scene.Scene; 
import javafx.stage.Stage; 
import javafx.scene.chart.LineChart; 
import javafx.scene.chart.NumberAxis; 
import javafx.scene.chart.XYChart; 

public class App extends Application{

    public static void Bitcoin() throws IOException, InterruptedException{
        while(true){

            //Opening and reading through website HTML
            URL url = new URL("https://www.google.com/finance/quote/BTC-USD");
            URLConnection urlconn = url.openConnection();
            InputStreamReader instream = new InputStreamReader(urlconn.getInputStream());
            BufferedReader buff = new BufferedReader(instream);
            String line = buff.readLine();
            String price= "not found";
            String priceb= "not found";

            //Reading through HTML to find price
                while(line != null){
                    if(line.contains("USD)\",3,null,")){
                        int target = line.indexOf("USD)\",3,null,");
                        int end = target;
                        int start = target;
                        while(line.charAt(end) != '.'){
                            end++;
                        }
                        while(line.charAt(start) != '['){
                            start++;
                        }
                        try{
                            price = line.substring(start+1, end+2);   
                        }catch(Exception e){
                            try{
                                price = line.substring(start+1, end+1);
                            }catch(Exception f){
                                price = line.substring(start+1, end);
                            }
                        }         
                    }
                    line=buff.readLine();
                }
                
                //Reading through text file to find previous price
                BufferedReader reader = new BufferedReader(new FileReader("src/Bitcoin.txt"));
                String line2;
                    while((line2 = reader.readLine()) != null){
                        int deci = line2.indexOf(".");
                        int start = deci;
                        while(line2.charAt(start)!=' '){
                            start--;
                        }
                        priceb= line2.substring(start+1,deci+3);           
                    } 

                //Adding price/time to text file if price has changed
                double pricedouble = Double.parseDouble(price);
                double pricebefore = Double.parseDouble(priceb);
                if(pricedouble != pricebefore){
                    FileWriter writer = new FileWriter("src/Bitcoin.txt", true);
                    DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();  
                    double pricechange= ((pricedouble-pricebefore)/pricebefore)*100;
                    writer.append(date.format(now)+" "+"Bitcoin: "+pricedouble+" %"+pricechange+"\n");
                    System.out.println(price);
                    pricebefore= pricedouble;
                    writer.close();
                    reader.close();
                }else{
                    while(pricebefore == pricedouble){
                        System.out.println("No change");
                        if(pricebefore != pricedouble){
                            break;
                        }
                        TimeUnit.SECONDS.sleep(5);
                    }
                }
        }
    }
    public static void SP() throws IOException, InterruptedException{
        while(true){

            //Opening and reading through website HTML
            URL url = new URL("https://www.google.com/finance/quote/.INX:INDEXSP?hl=en");
            URLConnection urlconn = url.openConnection();
            InputStreamReader instream = new InputStreamReader(urlconn.getInputStream());
            BufferedReader buff = new BufferedReader(instream);
            String line = buff.readLine();
            String price= "not found";
            String priceb= "not found";

            //Reading through HTML to find price
                while(line != null){
                    if(line.contains("\"INDEXSP\"],")){
                        int target = line.indexOf("\"INDEXSP\"],");
                        int deci = line.indexOf(".", target);
                        int start = deci;
                        while(line.charAt(start) != '['){
                            start--;
                        }
                        try{
                        price = line.substring(start+1, deci+2); 
                        }catch(Exception e){
                            price= line.substring(start+1, deci+1);
                        }                   
                    }
                    line=buff.readLine();
                }

                //Reading through text file to find previous price
                BufferedReader reader = new BufferedReader(new FileReader("src/SP500.txt"));
                String line2;
                    while((line2 = reader.readLine()) != null){
                        int deci = line2.indexOf(".");
                        int start = deci;
                        while(line2.charAt(start)!=' '){
                            start--;
                        }
                        priceb= line2.substring(start+1,deci+3);           
                    }

                //Adding price/time to text file if price has changed
                double pricedouble = Double.parseDouble(price);
                double pricebefore = Double.parseDouble(priceb);
                if(pricedouble != pricebefore){
                FileWriter writer = new FileWriter("src/SP500.txt", true);
                DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                double pricechange= ((pricedouble-pricebefore)/pricebefore)*100;
                writer.append(date.format(now)+" "+"S&P 500: "+pricedouble+" %"+pricechange+"\n");
                System.out.println(pricedouble);
                pricebefore=pricedouble;
                writer.close();
                reader.close();
                }else{
                    while(pricebefore == pricedouble){
                        System.out.println("No change");
                        if(pricebefore != pricedouble){
                            break;
                        }
                        TimeUnit.SECONDS.sleep(5);
                    }
                }
            }
            
    }

    public void start(Stage stage) throws IOException{

        //Define holding
        Scanner read = new Scanner(System.in);
        System.out.println("Enter (Bitcoin) or (SP500)");
        String holding = read.nextLine();
        while(!holding.equals("Bitcoin") && !holding.equals("SP500")){
            System.out.println("Invalid input, please enter (Bitcoin) or (SP500)");
            holding = read.nextLine();
        }
        read.close();

        //Defining file to get data from
        BufferedReader reader = new BufferedReader(new FileReader("src/"+holding+".txt"));
        BufferedReader reader1 = new BufferedReader(new FileReader("src/"+holding+".txt"));
        BufferedReader reader2 = new BufferedReader(new FileReader("src/"+holding+".txt"));
        BufferedReader dreader = new BufferedReader(new FileReader("src/"+holding+".txt"));

        //Defining start time
        String line2;
        line2 = reader.readLine();
        int deci = line2.indexOf("/");
        int start = deci;
        String bString1= line2.substring(start-4,deci+15); 
        String bString = bString1.replaceAll("/", "").replaceAll(":","").replaceAll(" ","");
        double bint = Double.parseDouble(bString);

        //Defining end time
        String line3;
        double aint =0;
        while((line3 = reader2.readLine()) != null){
            int deci1 = line3.indexOf("/");
            int start1 = deci1;
        String aString1= line3.substring(start1-4,deci1+15); 
        String aString = aString1.replaceAll("/", "").replaceAll(":","").replaceAll(" ","");
        aint = Double.parseDouble(aString);
        }

        //Defining highest stock value for y axis
        String linev;
        String svalue;
        double ivalue =1;
        double ivalueb =0;
        double ivaluelow= Double.MAX_VALUE;
        while((linev = reader1.readLine()) != null){
            int deciv = linev.indexOf(".");
            int startv = deciv;
            while(linev.charAt(startv)!=' '){
                startv--;
            }
            try{
                svalue= linev.substring(startv+1,deciv+3);
                ivalue = Double.parseDouble(svalue);
            }catch(Exception e){
                svalue= linev.substring(startv+1,deciv+2);
                ivalue = Double.parseDouble(svalue);
            }
            if(ivalue>ivalueb){
                ivalueb=ivalue;
            }
            if(ivaluelow>ivalue){
                ivaluelow=ivalue;
            }
        }

        //Defining the x axis   
        NumberAxis xAxis = new NumberAxis(bint, aint, 0); 
        xAxis.setLabel("Time (YYYYMMDDHHMMSS)");
        
        //Defining the y axis   
        NumberAxis yAxis = new NumberAxis(ivaluelow, ivalueb, ivalueb-ivaluelow); 
        yAxis.setLabel("Value in $USD"); 
          
        //Creating the line chart 
        LineChart linechart = new LineChart(xAxis, yAxis);  
          
        //Prepare XYChart.Series objects by setting data 
        XYChart.Series series = new XYChart.Series(); 
        series.setName("Price"); 
        String dline;
        String dprice;
        double dvalue = 0; 
        double datenum = 0;
        while((dline = dreader.readLine()) != null){
            int ddeci = dline.indexOf(".");
            int dstart = ddeci;
            while(dline.charAt(dstart)!=' '){
                dstart--;
            }
            try{
                dprice= dline.substring(dstart+1,ddeci+3);
                dvalue = Double.parseDouble(dprice);
            }catch(Exception e){
                try{dprice= dline.substring(dstart+1,ddeci+2);
                    dvalue = Double.parseDouble(dprice);
                }catch(Exception f){
                    dprice= dline.substring(dstart+1,ddeci+1);
                    dvalue = Double.parseDouble(dprice);
                }
            }
            int colon = dline.indexOf("/");
            int colstart = colon;
        String colstring= dline.substring(colstart-4,colon+15); 
        String newcolstring = colstring.replaceAll("/", "").replaceAll(":","").replaceAll(" ","");
        datenum = Double.parseDouble(newcolstring);
        
        series.getData().add(new XYChart.Data(datenum, dvalue)); 
        }  
        
              
        //Setting the data to Line chart    
        linechart.getData().add(series);        
          
        //Creating a Group object  
        Group root = new Group(linechart); 
           
        //Creating a scene object 
        Scene scene = new Scene(root, 600, 400);  
        
        //Setting title to the Stage 
        stage.setTitle("Line Chart"); 
           
        //Adding scene to the stage 
        stage.setScene(scene);
         
        //Displaying the contents of the stage 
        stage.show();   
        reader.close();
        reader1.close();
        reader2.close();
        dreader.close();
    } 
    public static void main(String[] args) throws IOException, InterruptedException{
        System.out.println("Run Bitcoin (1)");
        System.out.println("Run S&P500 (2)");
        System.out.println("Graph (3)");
        System.out.println("Enter a function: ");
        Scanner read = new Scanner(System.in);
        int func=read.nextInt();
        if(func==1){
            Bitcoin();
        }else if(func==2){
            SP();
        }else if(func==3){
            launch(args);
        }else{
            System.out.println("Invalid input");
            main(args);
        }
        read.close();
    }
}