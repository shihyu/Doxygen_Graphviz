#pragma once

#include "WatcherObserver.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace observer
			{
				namespace example5
				{


					///
					/// <summary> * ����ˮ�ʼ���Ŀ����� </summary>
					/// 
					class WaterQualitySubject
					{
					///	
					///	 <summary> * ��������ע��Ĺ۲��߶��� </summary>
					///	 
					protected:
						std::vector<WatcherObserver*> observers;
					///	
					///	 <summary> * ע��۲��߶��� </summary>
					///	 * <param name="observer"> �۲��߶��� </param>
					///	 
					public:
						virtual void attach(WatcherObserver *observer);
					///	
					///	 <summary> * ɾ���۲��߶��� </summary>
					///	 * <param name="observer"> �۲��߶��� </param>
					///	 
						virtual void detach(WatcherObserver *observer);
					///	
					///	 <summary> * ֪ͨ��Ӧ�Ĺ۲��߶��� </summary>
					///	 
						virtual void notifyWatchers() = 0;
					///	
					///	 <summary> * ��ȡˮ����Ⱦ�ļ��� </summary>
					///	 * <returns> ˮ����Ⱦ�ļ��� </returns>
					///	 
						virtual int getPolluteLevel() = 0;

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								observers = std::vector<WatcherObserver*>();

								initialized = true;
							}
						}

public:
	WaterQualitySubject()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}