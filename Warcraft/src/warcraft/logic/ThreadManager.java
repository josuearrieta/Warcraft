package warcraft.logic;

import java.util.ArrayList;

public class ThreadManager {
    //Manages all the threads of the program: start them, merge their results, etc. Just in the part of generating boats.
    private ThreadManager(){
        _SelectThreads = new SelectMovesThread[Runtime.getRuntime().availableProcessors()];//depending of the amount of procesor
        _MergeThreads = new MergeMovesThread[Runtime.getRuntime().availableProcessors()];
    }
    public static ThreadManager getInstance(){
        if (_Instance == null){
            _Instance = new ThreadManager();
        }
        return _Instance;
    }
    public Boat[] mainStrategy(){//call all the private methods below
        Integer amountOfProcessors = Runtime.getRuntime().availableProcessors();
        Integer amountOfRecordsPerProcessor = Constants.AMOUNT_OF_REGISTERS / amountOfProcessors;
        System.out.println(amountOfRecordsPerProcessor);
        Integer[] boatsId = Utility.generateBoatsId(amountOfProcessors);
        Boat[] boatList = new Boat[amountOfProcessors];
        for(int boatNumber = 0; boatNumber < amountOfProcessors; boatNumber++){
            //generates the strategy for all boats
            boatList[boatNumber] =
            mainSelectMovesForBoat(boatsId[boatNumber], amountOfProcessors, amountOfRecordsPerProcessor);
            
        }
        return boatList;
    }
    private Boat mainSelectMovesForBoat(Integer pBoatId, Integer pAmountProcesors, Integer pRecordsPerProcesor){
        //divides the 200000 records and send them to execute in parallel
        initializeSelectThreads(pAmountProcesors, pRecordsPerProcesor, pBoatId);
        waitForAllThreads();//waits for all selection to finish
        ArrayList<Move> allSelectedMoves = joinAnswerSelectThreads(pAmountProcesors);
        Integer movesPerMergeThread = allSelectedMoves.size() / pAmountProcesors;
        initializeMergeThreads(pAmountProcesors, movesPerMergeThread, allSelectedMoves);
        waitForAllThreads();//waits for all merge threads to finish
        ArrayList<Move> finalResult = joinAnswerMergeThreads(pAmountProcesors);
        System.out.println("Boat with id :  " + pBoatId + " was created, with : " + finalResult.size() + " different moves.");
        return new Boat(finalResult);//a whole new boat was created
    }
    private void initializeSelectThreads(Integer pAmountOfThreads, Integer amountRegistersPerThread, Integer pBoatId){
        Integer start = 0;
        Integer Final = amountRegistersPerThread;
        for(int threadNumber = 0 ; threadNumber < pAmountOfThreads ; threadNumber++){
            _SelectThreads[threadNumber] =  new SelectMovesThread(start,Final,pBoatId);
            start = Final;//so new ranges are set up for the following thread
            Final += amountRegistersPerThread;
            this._SelectThreads[threadNumber].start();
        }
    }    
    private void initializeMergeThreads(Integer pAmountOfThreads,Integer amountRegistersPerThread, ArrayList<Move> pList){
        Integer start = 0;
        Integer Final = amountRegistersPerThread;
        for(int threadNumber = 0 ; threadNumber < pAmountOfThreads ; threadNumber++){
            _MergeThreads[threadNumber] =  new MergeMovesThread(start, Final, pList);
            start = Final;//so new ranges are set up for the following thread
            Final += amountRegistersPerThread;
            _MergeThreads[threadNumber].start();
        }
    }
    
    private ArrayList<Move> joinAnswerSelectThreads(Integer pAmountOfThreads){
        ArrayList<Move>  result = new ArrayList();
        for(int threadNumber = 0 ; threadNumber < pAmountOfThreads ; threadNumber++){
            result.addAll(_SelectThreads[threadNumber].getProcessedMoves());
        }
        return result;
    }
    private ArrayList<Move> joinAnswerMergeThreads(Integer pAmountOfThreads){
        ArrayList<Move>  result = new ArrayList();
        for(int threadNumber = 0 ; threadNumber < pAmountOfThreads ; threadNumber++){
            result.addAll(_MergeThreads[threadNumber].getProcessedMoves());
        }
        return result;
    }
    private void waitForAllThreads(){
        //wats for all secundary threads to finish
        Integer sumator = 0;
        while(Thread.activeCount() != Constants.ACCEPTED_PROCESSOR_COUNT){//we will wait until all threads finish
            //System.out.println("Number of threads running " + Thread.activeCount());
            sumator++;//waste time
        }
    }
    private static ThreadManager _Instance = null;
    private SelectMovesThread[] _SelectThreads;//threads that will select the moves for a boat
    private MergeMovesThread[] _MergeThreads;//theads that will merge the moves of a boat
}
