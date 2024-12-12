/*
 * Copyright 2016 - 2019 Florian Spieß and the java-discord-rpc contributors
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

package ml.yuuki.chris.rpc.examples;

import com.discord.GameSDK.DiscordActivity;
import com.discord.GameSDK.DiscordActivityManager;
import com.discord.GameSDK.DiscordCore;
import com.discord.GameSDK.DiscordCreateFlags;
import com.discord.GameSDK.DiscordResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WindowTest {

	public static void main(String args[]) {
		if (args.length == 0) {
			System.err.println("You must specify an application ID in the arguments!");
			System.exit(-1);
		}
		DiscordCore core = new DiscordCore(args[0], DiscordCreateFlags.Default);
		DiscordActivityManager activityManager = core.getActivityManager();
		DiscordActivity activity = new DiscordActivity();
		activity.setDetails("Details here");
		activity.setState("State here");
		activity.timestamps().setStart(System.currentTimeMillis() / 1000);
		activity.timestamps().setEnd(activity.timestamps().getStart() + 20);
		activity.party().size().setCurrentSize(1);
		activity.party().size().setMaxSize(4);
		activityManager.updateActivity(activity, (result) -> {
			if (result != DiscordResult.Ok) {
				System.err.println("Failed to update activity: " + result);
			}
		});
		
		Thread t = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				core.runCallbacks();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					core.close();
					break;
				}
			}
		}, "RPC-Callback-Handler");
		t.start();
		
		JFrame frame = new JFrame("Java-Discord RPC");
		GridLayout frameLayout = new GridLayout(2, 1);
		frame.setLayout(frameLayout);
		
		JPanel top = new JPanel();
		GridLayout topLayout = new GridLayout(2, 2);
		top.setLayout(topLayout);
		
		JPanel bottom = new JPanel();
		GridLayout botLayout = new GridLayout(2, 3);
		bottom.setLayout(botLayout);
		
		
		// Details
		JLabel detailsLabel = new JLabel("Details");
		detailsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField detailsText = new JTextField(activity.getDetails());
		
		JLabel stateLabel = new JLabel("State");
		stateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField stateText = new JTextField(activity.getState());
		
		JTextField partySizeText = new JTextField(String.valueOf(activity.party().size().getCurrentSize()));
		JLabel partyLabel = new JLabel("of"); partyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField partyMaxText = new JTextField(String.valueOf(activity.party().size().getMaxSize()));
		
		JButton submit = new JButton("Update Presence");
		submit.addActionListener(e -> {
			activity.setDetails(detailsText.getText());
			activity.setState(stateText.getText());
			try {
				activity.party().size().setCurrentSize(Integer.parseInt(partySizeText.getText()));
			} catch (Exception ignored) {}
			try {
				activity.party().size().setMaxSize(Integer.parseInt(partyMaxText.getText()));
			} catch (Exception ignored) {} // if text isn't a number, ignore it
			
			activityManager.updateActivity(activity, (result) -> {
				if (result != DiscordResult.Ok) {
					System.err.println("Failed to update activity: " + result);
				}
			});
		});
		
		top.add(detailsLabel);
		top.add(detailsText);
		top.add(stateLabel);
		top.add(stateText);
		
		bottom.add(partySizeText);
		bottom.add(partyLabel);
		bottom.add(partyMaxText);
		bottom.add(new JPanel());
		bottom.add(submit);
		bottom.add(new JPanel()); // dummy components to center the update button
		
		frame.add(top);
		frame.add(bottom);
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				t.interrupt();
			}
		});
		
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}
