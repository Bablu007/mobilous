package com.mobilous.ext.plugin.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandLineTable {
	public static String response="";
    private static final String HORIZONTAL_SEP = "-";
    private String verticalSep;
    private String joinSep;
    private String[] headers;
    private List<String[]> rows = new ArrayList<>();
    private boolean rightAlign;

    public CommandLineTable() {
        setShowVerticalLines(false);
    }

    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    public void setShowVerticalLines(boolean showVerticalLines) {
        verticalSep = showVerticalLines ? "|" : "";
        joinSep = showVerticalLines ? "+" : " ";
    }

    public void setHeaders(String... headers) {
        this.headers = headers;
    }

    public void addRow(String... cells) {
        rows.add(cells);
    }

    public void print() {
        int[] maxWidths = headers != null ?
                Arrays.stream(headers).mapToInt(String::length).toArray() : null;

        for (String[] cells : rows) {
            if (maxWidths == null) {
                maxWidths = new int[cells.length];
            }
            if (cells.length != maxWidths.length) {
                throw new IllegalArgumentException("Number of row-cells and headers should be consistent");
            }
            for (int i = 0; i < cells.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], cells[i].length());
            }
        }

        if (headers != null) {
            printLine(maxWidths);
            printRow(headers, maxWidths);
            printLine(maxWidths);
        }
        for (String[] cells : rows) {
            printRow(cells, maxWidths);
            printLine(maxWidths);
            
        }
        if (headers != null) {
           // printLine(maxWidths);
        }
    }

    private void printLine(int[] columnWidths) {
        for (int i = 0; i < columnWidths.length; i++) {
            String line = String.join("", Collections.nCopies(columnWidths[i] +
                    verticalSep.length() + 1, HORIZONTAL_SEP));
            System.out.print(joinSep + line + (i == columnWidths.length - 1 ? joinSep : ""));
            response+=joinSep + line + (i == columnWidths.length - 1 ? joinSep : "");
        }
        
        System.out.println();
        response+="\n";
    }

    private void printRow(String[] cells, int[] maxWidths) {
        for (int i = 0; i < cells.length; i++) {
            String s = cells[i];
            String verStrTemp = i == cells.length - 1 ? verticalSep : "";
            if (rightAlign) {
                System.out.printf("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp) ;
                response+=String.format("%s %" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp);
            } else {
                System.out.printf("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp) ;
                response+=String.format("%s %-" + maxWidths[i] + "s %s", verticalSep, s, verStrTemp) ; 
            }
        }
        
        System.out.println();
        response+="\n";
    }
    
    public static void main(String [] args) {
    	
    	call("yash","yash","yash","yash");
    	System.out.println(response);
    	String text=
    	         "<table width='100%' border='1' align='center'>"
    	                + "<tr align='center'>"
    	                + "<td><b>Product Name <b></td>"
    	                + "<td><b>Count<b></td>"
    	                + "</tr>";
    	System.out.println(text);
    }
    

    public static String call(String old_cat, String new_cat, String old_subcat, String new_subcat) {
       
        CommandLineTable st = new CommandLineTable();
        //st.setRightAlign(true);//if true then cell text is right aligned
        st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
        st.setHeaders("Header", "Existing", "Requested");//optional - if not used then there will be no header and horizontal lines
        st.addRow("Category", old_cat, new_cat);
        st.addRow("Sub-category", old_cat, new_subcat);
        st.print();
        return response;
    }}