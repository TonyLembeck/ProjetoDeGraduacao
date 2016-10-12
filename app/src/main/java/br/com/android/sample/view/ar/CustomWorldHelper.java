/*
 * Copyright (C) 2014 BeyondAR
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.android.sample.view.ar;

import android.annotation.SuppressLint;
import android.content.Context;


import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.util.LibraryClass;


@SuppressLint("SdCardPath")
public class CustomWorldHelper {
	public static final int LIST_TYPE_EXAMPLE_1 = 1;

	public static World sharedWorld;
	private static DatabaseReference firebaseRef;
	private static long l = 0;
	public static World generateObjects(Context context, double latitude, double longitude) {
		if (sharedWorld != null) {
			return sharedWorld;
		}

		sharedWorld = new World(context);

		// The user can set the default bitmap. This is useful if you are
		// loading images form Internet and the connection get lost
		sharedWorld.setDefaultImage(R.drawable.beyondar_default_unknow_icon);

		// User position (you can change it using the GPS listeners form Android
		// API)
		sharedWorld.setGeoPosition(latitude, longitude);

		firebaseRef = LibraryClass.getFirebase();
		firebaseRef = firebaseRef.child("pontos");


		firebaseRef.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				String ref = dataSnapshot.getKey();
				DatabaseReference novaRef;
				novaRef = firebaseRef.child(ref);


				novaRef.addValueEventListener(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot dataSnapshot) {

						Ponto ponto = dataSnapshot.getValue(Ponto.class);

						GeoObject go = new GeoObject(l++);
						go.setGeoPosition(ponto.getLatitude(), ponto.getLongitude());
						go.setImageResource(R.drawable.logocponto);
						go.setName(ponto.getNome());
						sharedWorld.addBeyondarObject(go);
					}

					@Override
					public void onCancelled(DatabaseError databaseError) {

					}
				});
			}

			@Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
			@Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
			@Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
			@Override public void onCancelled(DatabaseError databaseError) {}

		});

		/*
		// Create an object with an image in the app resources.
		GeoObject go1 = new GeoObject(1l);
		go1.setGeoPosition(41.90523339794433d, 2.565036406654116d);
		go1.setImageResource(R.drawable.creature_1);
		go1.setName("Creature 1");

		// Is it also possible to load the image asynchronously form internet
		GeoObject go2 = new GeoObject(2l);
		go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
		go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
		go2.setName("Online image");

		// Also possible to get images from the SDcard
		GeoObject go3 = new GeoObject(3l);
		go3.setGeoPosition(41.90550959641445d, 2.565873388087619d);
		go3.setImageUri("/sdcard/someImageInYourSDcard.jpeg");
		go3.setName("IronMan from sdcard");

		// Add the GeoObjects to the world
		sharedWorld.addBeyondarObject(go1);
		sharedWorld.addBeyondarObject(go2, LIST_TYPE_EXAMPLE_1);
		sharedWorld.addBeyondarObject(go3);
*/
		return sharedWorld;
	}
}
