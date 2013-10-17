#pragma once

#include "WatcherObserver.h"
#include "WaterQualitySubject.h"
#include <string>

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
					/// <summary> * ����Ĺ۲���ʵ�� </summary>
					/// 
					class Watcher : public WatcherObserver
					{
					///	
					///	 <summary> * ְ�� </summary>
					///	 
					private:
						std::string job;

					public:
						virtual void update(WaterQualitySubject *subject);

						virtual std::string getJob();

						virtual void setJob(std::string job);
					};

				}
			}
		}
	}
}