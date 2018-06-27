# Android_RelatedEntities
Helper class for using relations in Room (Android)

This is a helper class for use in with Android Room DAO's. It simplifies creating nested tables
with related entities, where you might want to control the query (which you canno't do using
a Room @Relation. The concept was taken from how the generated room dao's add their relations.
Note: Maximum query bind size is 999 items so there are limits to this class.

Another advantage in larger projects is that you don't need to create separate POJO's for each
different related table, ie: UserWithPet, UserWithDetails, UserWithHouse, UserWithHouseWithDoors
etc. Instead, include all required related entities in the User table each with an @Ignore
annotation. Then, instead use different DAO calls to populate the classes as required.

Entities need to implement the IRowID interface for getting the Row ID value.

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

Eg: Simple entity class and DAO

public class User implements IRowID {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id", index = true)
    public long mId;

    @ColumnInfo(name = "alias")
    public String mAlias;

    @Ignore
    public List<Details> mDetails;

    @Ignore
    public List<Pet> mPets;

    @Ignore
    public List<House> mHouse;

    @Override
    public long getId() {
        return mId;
    }
}

public class House implements IRowID {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id", index = true)
    public long mId;

    @ColumnInfo(name = "userid", index = true)
    public long mUserId;

    @ColumnInfo(name = "alias")
    public String mAlias;

    @ColumnInfo(name = "streetnumber")
    public String mStreetNumber;

    @Ignore
    public List<Doors> mDoors;

    @Override
    public long getId() {
        return mId;
    }
}

public class Doors implements IRowID {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id", index = true)
    public long mId;

    @ColumnInfo(name = "houseid", index = true)
    public long mHouseId;

    @ColumnInfo(name = "facing")
    public String mFacing;

    @ColumnInfo(name = "colour")
    public String mColour;

    @Override
    public long getId() {
        return mId;
    }
}

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
 */
