package nz.co.performaplus.data.utils;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
    Copyright 2018 Lincoln Rogers

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

public class RelatedCollection<TP extends RelatedCollection.IRowID, TC> {
    private ArrayMap<Long, ArrayList<TC>> fullCollection = new ArrayMap<>();
    private Long[] parentIds;

    public interface IRowID {
        long getId();
    }

    public interface LinkToParent<TP, TC>{
        void apply(TP parent, ArrayList<TC> child);
    }

    public interface LinkOnChildId<TC>{
        long apply(TC child);
    }

    public RelatedCollection(List<TP> inpList, LinkToParent<TP, TC> linkToParent){
        //Map related collections to each inpList item, keyed to id
        for(TP item : inpList){
            long key = item.getId();
            ArrayList<TC> itemCollection = fullCollection.get(key);
            if(itemCollection == null){
                itemCollection = new ArrayList<>();
                fullCollection.put(key, itemCollection);
            }
            linkToParent.apply(item, itemCollection);                                               //Perform action of connecting the subTable collection to inpList item
        }

        //Get list of parent id's to get for the related collection
        final Set<Long> inpListKeySet = fullCollection.keySet();
        parentIds = inpListKeySet.toArray(new Long[ inpListKeySet.size() ]);
    }

    public Long[] getIds(){
        return parentIds;
    }

    public void addChildList(List<TC> childList, LinkOnChildId<TC> linkOnChildId){
        for(TC child : childList){
            fullCollection.get(linkOnChildId.apply(child)).add(child);                              //Lambda to return the child table's relation id column
            }
    }
}

