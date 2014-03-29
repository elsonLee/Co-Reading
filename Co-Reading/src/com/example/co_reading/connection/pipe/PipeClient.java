/*Copyright (C) 2014  ElsonLee & WenPin Cui

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	
package com.example.co_reading.connection.pipe;

import java.io.IOException;

import com.example.co_reading.connection.Client;
import com.example.co_reading.util.ISerialization;

public class PipeClient extends Client {
	
	private String TAG = PipeClient.class.getSimpleName();

	public PipeClient() throws IOException {
		super();
	}
	
	public PipeClient(ISerialization serialization) throws IOException {
		super(serialization);
	}

	public void cancel() {

	}
	
	/**
	 * initialize(InputStream, OutputStream, 
	 */
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}