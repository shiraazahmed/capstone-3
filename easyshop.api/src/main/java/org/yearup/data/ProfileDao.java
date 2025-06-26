package org.yearup.data;


import org.yearup.models.Profile;

import java.sql.SQLException;

public interface ProfileDao
{
    Profile create(Profile profile);

}
