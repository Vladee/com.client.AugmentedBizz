package com.app.augmentedbizz.application.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.init.Initializer;
import com.app.augmentedbizz.cache.CacheDbAdapter;
import com.app.augmentedbizz.cache.CacheStorageManager;
import com.app.augmentedbizz.cache.CacheResponseListener;
import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.services.ServiceManager;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity.TargetIndicator;
import com.app.augmentedbizz.services.handler.ServiceHandlingException;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.service.BaseHttpService;
import com.app.augmentedbizz.services.service.repository.IndicatorHttpService;
import com.app.augmentedbizz.services.service.repository.ModelHttpService;
import com.app.augmentedbizz.services.service.repository.TargetHttpService;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

/**
 * The data manager presents an interface between the application facade, the cache and the service manager
 * and acts as the central point for dataflows inside the application.
 * 
 * @author Vladi
 *
 */
public class DataManager implements ServiceResponseListener, ModelDataListener, TargetDataListener, IndicatorDataListener, CacheResponseListener {
	
	private Target currentTarget = null;
	private List<TargetIndicator> indicators = null;
	private OpenGLModelConfiguration openGLModelConfiguration = null;
	private ServiceManager serviceManager;
	private EntityConverter entityConverter;
	private ApplicationFacade facade;
	private CacheStorageManager cacheManager;
	
	private List<ModelDataListener> modelDataListeners = new ArrayList<ModelDataListener>();
	private List<TargetDataListener> targetDataListeners = new ArrayList<TargetDataListener>();
	private List<IndicatorDataListener> indicatorDataListeners = new ArrayList<IndicatorDataListener>();
	
	public DataManager(ApplicationFacade facade) {
		this.serviceManager = new ServiceManager(facade);
		this.entityConverter = new EntityConverter();
		this.facade = facade;
		this.cacheManager = new CacheStorageManager(facade.getContext());
		
		this.addModelDataListener(this);
		this.addTargetDataListener(this);
		this.addIndicatorDataListener(this);
	}
	
	/**
	 * @return the application facade
	 */
	public ApplicationFacade getApplicationFacade() {
		return facade;
	}

	public void addModelDataListener(ModelDataListener listener) {
		this.modelDataListeners.add(listener);
	}
	
	public void removeModelDataListener(ModelDataListener listener) {
		this.modelDataListeners.remove(listener);
	}
	
	public void addTargetDataListener(TargetDataListener listener) {
		this.targetDataListeners.add(listener);
	}
	
	public void removeTargetDataListener(TargetDataListener listener) {
		this.targetDataListeners.remove(listener);
	}
	
	public void addIndicatorDataListener(IndicatorDataListener listener) {
		this.indicatorDataListeners.add(listener);
	}
	
	public void removeIndicatorDataListener(IndicatorDataListener listener) {
		this.indicatorDataListeners.remove(listener);
	}
	
	private void fireOnModelDataEvent(OpenGLModelConfiguration openGLModelConfiguration) {
		Iterator<ModelDataListener> it = this.modelDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onModelData(openGLModelConfiguration);
		}
	}
	
	private void fireOnModelErrorEvent(Exception e) {
		Iterator<ModelDataListener> it = this.modelDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onModelError(e);
		}
	}
	
	private void fireOnTargetDataEvent(Target target) {
		Iterator<TargetDataListener> it = this.targetDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onTargetData(target);
		}
	}
	
	private void fireOnTargetErrorEvent(Exception e) {
		Iterator<TargetDataListener> it = this.targetDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onTargetError(e);
		}
	}
	
	private void fireOnIndicatorDataEvent(List<TargetIndicator> indicators) {
		Iterator<IndicatorDataListener> it = this.indicatorDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onIndicatorData(indicators);
		}
	}
	
	private void fireOnIndicatorErrorEvent(Exception e) {
		Iterator<IndicatorDataListener> it = this.indicatorDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onIndicatorError(e);
		}
	}
	
	public void loadModel(int modelId) {
		//check for local buffer
		if(this.openGLModelConfiguration == null) {
			//try to read from buffer
			cacheManager.readModelAsync(modelId, this);
		} else {
			this.fireOnModelDataEvent(this.openGLModelConfiguration);
		}
	}
	
	public void loadTarget(int targetId) {
		//check the local buffer
		if(this.currentTarget != null) {
			this.fireOnTargetDataEvent(this.currentTarget);
		} else {
			this.serviceManager.callTargetInformationService(targetId, this);
		}
	}
	
	public void loadIndicators(int targetId) {
		//check the local buffer
		if(this.indicators != null) {
			this.fireOnIndicatorDataEvent(this.indicators);
		} else {
			this.serviceManager.callIndicatorInformationService(targetId, this);
		}
	}
	
	private void handleModelResponse(ServiceTransferEntity stEntity, BaseHttpService calledService) {
		//convert service entity to model
		OpenGLModelConfiguration model = entityConverter.toOpenGLModelFrom(stEntity, calledService, this.currentTarget.getLatestModelVersion());
		//insert into cache
		cacheManager.insertOrUpdateModelAsync(model);
		
		this.fireOnModelDataEvent(model);
	}
	
	private void handleTargetResponse(ServiceTransferEntity stEntity) {
		this.fireOnTargetDataEvent(this.entityConverter.toTargetFrom(stEntity));
	}
	
	private void handleIndicatorResponse(ServiceTransferEntity stEntity) {
		this.fireOnIndicatorDataEvent(this.entityConverter.toIndicatorListFrom(stEntity));
	}

	@Override
	public void onServiceResponse(ServiceTransferEntity stEntity,
			BaseHttpService calledService) {
		if(calledService instanceof ModelHttpService) {
			this.handleModelResponse(stEntity, calledService);
		} else
		if(calledService instanceof TargetHttpService) {
			this.handleTargetResponse(stEntity);
		} else {
			this.handleIndicatorResponse(stEntity);
		}
	}

	@Override
	public void onServiceFailed(ServiceHandlingException exception,
			BaseHttpService calledService) {
		DebugLog.loge("Unable to call service.", exception);
		
		if(calledService instanceof ModelHttpService) {
			this.fireOnModelErrorEvent(exception);
		} else if(calledService instanceof TargetHttpService) {
			this.fireOnTargetErrorEvent(exception);
		} else if(calledService instanceof IndicatorHttpService) {
			this.fireOnIndicatorErrorEvent(exception);
		}
	}

	@Override
	public void onModelData(OpenGLModelConfiguration openGLModelConfiguration) {
		this.openGLModelConfiguration = openGLModelConfiguration;
	}

	@Override
	public void onModelError(Exception e) {
		getApplicationFacade().getUIManager().showWarningToast(R.string.errorModelRetrieval);
	}

	@Override
	public void onIndicatorData(List<TargetIndicator> targetIndicators) {
		this.indicators = targetIndicators;
	}
	@Override
	public void onIndicatorError(Exception e) {
		getApplicationFacade().getUIManager().showWarningToast(R.string.errorServiceUnreachable);
	}
	
	@Override
	public void onTargetData(Target target) {
		this.currentTarget = target;
	}

	@Override
	public void onTargetError(Exception e) {
		getApplicationFacade().getUIManager().showWarningToast(R.string.errorServiceUnreachable);
	}
	
	@Override
	public void onLoadedModelConfig(OpenGLModelConfiguration model) {
		//retrieved the model successfully from cache
		fireOnModelDataEvent(model);
	}

	@Override
	public void onFailedModelConfigLoading(int modelId) {
		//try to get the data from the service manager
		this.serviceManager.callModelInformationService(modelId, this);
	}

	/**
	 * @return the current target from the local buffer
	 */
	public Target getCurrentTarget() {
		return currentTarget;
	}

	/**
	 * @return the indicators
	 */
	public List<TargetIndicator> getCurrentIndicators() {
		return indicators;
	}
	
}
