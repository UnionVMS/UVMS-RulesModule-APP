package eu.europa.ec.fisheries.uvms.rules.service;

import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class XMLCutter {

    private static final Logger log = Logger.getLogger("LiquibaseUtilLogger");

    static {
        log.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        log.addHandler(handler);
    }


    @Test
    @Ignore
    public void createXMLLiquibaseEntries() throws IOException {
        String filecontentStr = IOUtils.toString(new FileInputStream("src/test/resources/testData/changelog.xml"));
        List<String> filesContents = Arrays.asList(filecontentStr.split("<insert"));

        List<String> completeInsertionsElements = new ArrayList<>();
        filesContents.forEach(insertion -> {
            String insert = "<insert" + insertion.replaceFirst("name=\"context\" value=\"EU\"", "name=\"context\" value=\"NEAFC\"");
            completeInsertionsElements.add(insert);
        });
        List<String> existingCodes = getExistingCodes();
        List<String> neededInsertionsElements = new ArrayList<>();
        existingCodes.forEach(code -> {
            String filter = "name=\"rule_id\" value=\""+code+"\"";

            String id = "<column name=\"id\" value=\""+code+"\"/>";
            String idSub = "<column name=\"id\" value=\"20000"+code+"\"/>";

            String rid = "<column name=\"rule_id\" value=\""+code+"\"/>";
            String ridSub = "<column name=\"rule_id\" value=\"20000"+code+"\"/>";

            String foundPiece = completeInsertionsElements.stream().filter(elem -> elem.contains(filter)).findFirst().get();
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(foundPiece)){
                String finalPiece = foundPiece.replaceFirst(id, idSub).replaceFirst(rid, ridSub);
                neededInsertionsElements.add(finalPiece);
            }
        });

        final StringBuffer buf = new StringBuffer();
        neededInsertionsElements.forEach(elem -> buf.append(elem));

        String finalFileContent = buf.toString();
        //createNewFile();

        log.log(Level.ALL, "\n\n-->>>> All the work for file creation ended successfully. \n-->>>> You can get your files at : " + finalFileContent);
    }

    @Test
    @Ignore
    public void countMissingRules() throws IOException {
        String filecontentStr = IOUtils.toString(new FileInputStream("src/test/resources/testData/changelog_1.xml"));
        String[] split = filecontentStr.split("<insert");
        log.log(Level.ALL, "\n\n-->> All codes size  : " + split.length);
        List<String> existingCodes = getExistingCodes();
        existingCodes.forEach(code -> {
            String codeNew = "20000" + code;
            if(!filecontentStr.contains(codeNew)){
                log.log(Level.ALL, "\n\n-->> The following code was not found : " + code);
            }
        });
    }

    private List<String> getExistingCodes() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7",
                "8", "9", "10", "14", "15", "16", "17", "20", "21", "22", "25", "26", "27", "28", "30",
                "32", "33", "34", "35", "36", "37", "39", "40", "41", "42", "43", "44", "45", "46", "47", "51", "52",
                "55", "56", "57", "58", "59", "60", "62", "67", "69", "70", "72", "74", "76", "77",
                "90", "91", "92", "94", "97", "101", "102", "103", "104", "105", "106", "150", "151", "152",
                "153", "154", "155", "160", "161", "166", "167", "190", "192", "195", "196", "197", "201",
                "203", "204", "205", "206", "216", "207", "210", "120", "121", "124", "125", "126", "128",
                "129", "130", "145", "146", "131", "132", "135", "136", "137", "220", "221", "223",
                "229", "224", "225", "226", "147", "148", "227", "228", "256", "260", "610", "281", "321", "322",
                "323", "291", "292", "293", "294", "295", "296", "297", "298", "380", "381", "382", "383", "384",
                "385", "386", "387", "388", "389", "390", "391", "553", "368", "392", "393", "394", "395", "396", "555",
                "554", "397", "398", "399", "400", "401", "402", "403", "406", "404", "405");
    }


    private static void createNewFile(String filePath, String content) {
        File fileEntry = new File(filePath);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileEntry), Charsets.UTF_8))) {
            writer.write(content);
        } catch (UnsupportedEncodingException e) {
            log.log(Level.ALL, "UnsupportedEncodingException : ", e);
        } catch (FileNotFoundException e) {
            log.log(Level.ALL, "FileNotFoundException : ", e);
        } catch (IOException e) {
            log.log(Level.ALL, "IOException : ", e);
        }
    }


}
