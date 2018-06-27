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

/*
 * This dao will fetch users with houses with red-doors.
 * The RXJava flowable is maintained and will emit when any of the tables are updated.
 */

public Flowable<List<User>> getUserHouseDoors(long userId) {
    return getUserById(userId).flatMap(users -> {
        RelatedCollection<User, House> userAddHouses = new RelatedCollection<>(users, (parent, child) -> parent.mHouse = child);                                        //Map house collections to each user, keyed to id

        //Get Houses and add to parent collections, then continue by adding Doors
        return getHousesById(userAddHouse.getIds()).flatMap(houses -> {
            userAddHouses.addChildList(houses, child -> child.mUserId);                                                                                                 //Add collections to parent
            RelatedCollection<House, Door> houseAddDoors = new RelatedCollection<>(houses, (parent, child) -> parent.mDoors = child);                                   //Map door collections to each house, keyed to id

            //Get Doors and add to parent collections
            return getDoorsByIdAndColour(houseAddDoors.getIds(), "Red").flatMap(doors -> {
                houseAddDoors.addChildList(doors, child -> child.mHouseId);                                                                                             //Add collections to parent
                return Flowable.just(users);
            });
        });
    });
}
