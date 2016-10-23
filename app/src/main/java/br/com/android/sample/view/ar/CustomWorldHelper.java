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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.beyondar.android.util.ImageUtils;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.BeyondarObjectList;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import br.com.android.sample.R;
import br.com.android.sample.domain.Ponto;
import br.com.android.sample.domain.util.LibraryClass;


@SuppressLint("SdCardPath")
public class CustomWorldHelper extends AppCompatActivity{
	public static final int LIST_TYPE_EXAMPLE_1 = 1;
	protected static ArrayList<Ponto> pontos = new ArrayList<>();
	private static final String TMP_IMAGE_PREFIX = "viewImage_";
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
		sharedWorld.setDefaultImage(R.mipmap.logo_fundo_azul);

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
						pontos.add(ponto);
						GeoObject go = new GeoObject(l);
						go.setDistanceFromUser(2);
						go.setGeoPosition(ponto.getLatitude(), ponto.getLongitude());
						//go.setImageResource(R.mipmap.logo_fundo_azul);
						go.setName(ponto.getNome());
						l++;
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

		return sharedWorld;
	}

	public static Ponto getPonto(long id){

		int indice = (int) id;

		return pontos.get(indice);
	}
}
