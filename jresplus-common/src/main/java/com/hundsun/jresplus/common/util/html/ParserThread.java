package com.hundsun.jresplus.common.util.html;


/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ParserThread extends Thread {

	private final static Logger logger = LoggerFactory.getLogger(ParserThread.class);

	HTMLParser parser;

	ParserThread(HTMLParser p) {
		parser = p;
	}

	public void run() { // convert pipeOut to pipeIn
		try {
			try { // parse document to pipeOut
				parser.HTMLDocument();
			} catch (ParseException e) {
				logger.error("error then parse html", e);
			} catch (TokenMgrError e) {
				logger.error("error then parse html", e);
			} finally {
				parser.pipeOut.close();
				synchronized (parser) {
					parser.summary.setLength(HTMLParser.SUMMARY_LENGTH);
					parser.titleComplete = true;
					parser.notifyAll();
				}
			}
		} catch (IOException e) {
			logger.error("error then parse html", e);
		}
	}
}
