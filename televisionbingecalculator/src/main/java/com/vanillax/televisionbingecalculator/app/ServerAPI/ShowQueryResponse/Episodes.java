package com.vanillax.televisionbingecalculator.app.ServerAPI.ShowQueryResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mitch on 6/8/14.
 */
public class Episodes
{
	@SerializedName( "season" )
	public int season;

	@SerializedName( "number" )
	public int number;
}