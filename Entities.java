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

