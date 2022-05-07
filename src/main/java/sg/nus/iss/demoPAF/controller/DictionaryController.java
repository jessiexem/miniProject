package sg.nus.iss.demoPAF.controller;

//@Controller
public class DictionaryController {

//    @Autowired
//    private DictionaryService dictionaryService;
//
//    @PostMapping("/search")
//    public ModelAndView searchDictionary(@RequestBody MultiValueMap<String,String> payload) {
//        System.out.println(">>>>> in searchDictionary() controller");
//        String term = payload.getFirst("search");
//
//        ModelAndView mav = new ModelAndView();
//
//        Optional<List<Word>> opt = dictionaryService.searchWord(term);
//
//        if(opt.isEmpty()) {
//            mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            mav.setViewName("404");
//            return mav;
//        }
//
//        List<Word> wordList = opt.get();
//        System.out.println("--------controller: wordList retrieved");
//
//
//        mav.addObject("word",term);
//        mav.addObject("wordList",wordList);
//        mav.setViewName("searchresult");
//        return mav;
//    }
}
