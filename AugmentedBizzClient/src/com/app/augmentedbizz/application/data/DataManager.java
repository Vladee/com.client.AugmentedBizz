package com.app.augmentedbizz.application.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.app.augmentedbizz.cache.CacheDbAdapter;
import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.services.ServiceManager;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity.TargetIndicator;
import com.app.augmentedbizz.services.handler.ServiceHandlingException;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.service.BaseHttpService;
import com.app.augmentedbizz.services.service.repository.ModelHttpService;
import com.app.augmentedbizz.services.service.repository.TargetHttpService;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;
import com.app.augmentedbizz.ui.renderer.Target;

/**
 * The data manager presents an interface between the application facade, the cache and the service manager
 * and acts as the central point for dataflows inside the application.
 * 
 * @author Vladi
 *
 */
public class DataManager implements ServiceResponseListener, ModelDataListener, TargetDataListener, IndicatorDataListener {
	
	private Target currentTarget = null;
	private List<TargetIndicator> indicators = null;
	private OpenGLModelConfiguration openGLModelConfiguration = null;
	private ServiceManager serviceManager;
	private EntityConverter entityConverter;
	private Context context;
	
	private List<ModelDataListener> modelDataListeners = new ArrayList<ModelDataListener>();
	private List<TargetDataListener> targetDataListeners = new ArrayList<TargetDataListener>();
	private List<IndicatorDataListener> indicatorDataListeners = new ArrayList<IndicatorDataListener>();
	
	public DataManager(Context context) {
		this.serviceManager = new ServiceManager(context);
		this.entityConverter = new EntityConverter();
		this.context = context;
		
		this.addModelDataListener(this);
		this.addTargetDataListener(this);
		this.addIndicatorDataListener(this);
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
	
	private void fireOnIndicatorDataEvent(List<TargetIndicator> indicators) {
		Iterator<IndicatorDataListener> it = this.indicatorDataListeners.iterator();
		
		while(it.hasNext()) {
			it.next().onIndicatorData(indicators);
		}
	}
	
	public void loadModel(int modelId) {
		this.serviceManager.callModelInformationService(modelId, this);
		if(this.openGLModelConfiguration == null) {
			CacheDbAdapter cache = new CacheDbAdapter(this.context);
			this.openGLModelConfiguration = cache.fetchModel(modelId);
		}
		if(this.openGLModelConfiguration != null) {
			this.fireOnModelDataEvent(this.openGLModelConfiguration);
		}
	}
	
	public void loadTarget(int targetId) {
		this.serviceManager.callTargetInformationService(targetId, this);
		if(this.currentTarget != null) {
			this.fireOnTargetDataEvent(this.currentTarget);
		}
	}
	
	public void loadIndicators(int targetId) {
		this.serviceManager.callIndicatorInformationService(targetId, this);
		if(this.indicators != null) {
			this.fireOnIndicatorDataEvent(this.indicators);
		}
	}
	
	private void handleModelResponse(ServiceTransferEntity stEntity,
			BaseHttpService calledService) {
		this.fireOnModelDataEvent(this.entityConverter.toOpenGLModelFrom(stEntity,
				calledService, this.currentTarget.getLatestModelVersion()));
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
		}
	}

	@Override
	public void onModelData(OpenGLModelConfiguration openGLModelConfiguration) {
		this.openGLModelConfiguration = openGLModelConfiguration;
	}

	@Override
	public void onModelError(Exception e) {
		// relax
	}

	@Override
	public void onIndicatorData(List<TargetIndicator> targetIndicators) {
		this.indicators = targetIndicators;
	}

	@Override
	public void onTargetData(Target target) {
		this.currentTarget = target;
	}
	
}
