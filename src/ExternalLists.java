import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExternalLists {
    private Map<String, Set<String>> externalListsMap;

    private Set<String> companySuffixes = new HashSet<>();
    public Set<String> nounPosTags = new HashSet<>();
    public Set<String> namePrefixes = new HashSet<>();

    public ExternalLists() throws Exception{
        Set<String> maleNameSet = new HashSet<>();
        Set<String> femaleNameSet = new HashSet<>();
        Set<String> placeNameSet = new HashSet<>();
        Set<String> countryListSet = new HashSet<>();
        Set<String> allFirstNames = new HashSet<>();
        Set<String> allLastNames = new HashSet<>();
        Set<String> englishStopList = new HashSet<>();
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\female_names.txt", femaleNameSet);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\Male_names.txt", maleNameSet);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\us_cities_states_counties.txt", placeNameSet);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\company_suffixes.txt", companySuffixes);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\nounPosTags.txt", nounPosTags);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\countrylist.txt", countryListSet);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\name_prefixes.txt", namePrefixes);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\allfirstnames.txt", allFirstNames);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\alllastnames.txt", allLastNames);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\alllastnames.txt", allLastNames);
        generatenameSet("D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\external_data\\stoplist.txt", englishStopList);
        externalListsMap = new HashMap<>();
        femaleNameSet.addAll(maleNameSet);
//        placeNameSet.addAll(countryListSet);
        externalListsMap.put("fname", femaleNameSet);
//        externalListsMap.put("mname", maleNameSet);
//        externalListsMap.put("place", placeNameSet);
        externalListsMap.put("countrylst", countryListSet);
        allFirstNames.addAll(allLastNames);
        externalListsMap.put("firstNames", allFirstNames);
//        externalListsMap.put("stoplist", englishStopList);
//        externalListsMap.put("lastNames", allLastNames);
    }

    private void generatenameSet(String fileName, Set<String> set) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        String name;
        while((line = br.readLine())!=null){
            if(!"".equals(line.trim())) {
                name = line.split("\t")[0];
                set.add(name.toUpperCase());
            }
        }
        br.close();
    }

    public Map<String, Set<String>> getExternalListsMap() {
        return externalListsMap;
    }

    public Set<String> getCompanySuffixes() {
        return companySuffixes;
    }
}
