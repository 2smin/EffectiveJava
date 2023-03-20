package Chapter03.item14;

import java.io.Serializable;
import java.util.List;

public class Posting implements Comparable<Posting>, Serializable {
    private Long id;
    private int termFrequency;
    private List<Integer> startOffsets;
    private List<Integer> endOffsets;

    public void increaseTermFrequency(){
        this.termFrequency = this.termFrequency + 1;
    }

    public int getTermFrequency(){
        return this.termFrequency;
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public int compareTo(Posting posting){
        if(posting.termFrequency < termFrequency){
            return -1;
        }
        else if(posting.termFrequency > termFrequency){
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if(o == null || getClass() != o.getClass()){
            return false;
        }
        if(!this.id.equals(((Posting)o).getId())){
            return false;
        }
        return true;
    }

    private boolean isUpdated(Object o){
        if(this.equals(o)){
            if(!(this.termFrequency == (((Posting)o).getTermFrequency()))){
                return true;
            }
        }
        return false;
    }
}
