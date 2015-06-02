package warcraft.logic;


public class Move {
    private Double _Degree;
    private Integer _Id;
    private String _Action;
    private Double _Value;
    
    public Move(Double pDegree, Integer pId, String pAction, Double pValue){
        _Degree = pDegree;
        _Id = pId;
        _Action = pAction;
        _Value = pValue;
    }
    public Double getDegree(){
        return _Degree;
    }
    public Integer getId(){
        return _Id;
    }
    public String getAction(){
        return _Action;
    }
    public Double getValue(){
        return _Value;
    }
    @Override
    public String toString(){
        return _Degree + "|" + _Id + "|" + _Action + "|" + _Value;
     }
}
