package com.derek_s.hubble_gallery.internal.di;

import com.derek_s.hubble_gallery.utils.dagger.PerApp;

import dagger.Component;

/**
 * Created by derek on 15-08-03.
 */

@PerApp
@Component(modules = {SystemServicesModule.class})
public interface HubbleComponent extends CoreGraph {
}
