#pragma once

#include "WaterQualitySubject.h"

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
					/// <summary> * ˮ�ʹ۲��߽ӿڶ��� </summary>
					/// 
					class WatcherObserver
					{
					///	
					///	 <summary> * ��֪ͨ�ķ��� </summary>
					///	 * <param name="subject"> ���뱻�۲��Ŀ����� </param>
					///	 
					public:
						virtual public void update(WaterQualitySubject*) = 0;
					///	
					///	 <summary> * ���ù۲���Ա��ְ�� </summary>
					///	 * <param name="job"> �۲���Ա��ְ�� </param>
					///	 
						virtual public void setJob(std::string) = 0;
					///	
					///	 <summary> * ��ȡ�۲���Ա��ְ�� </summary>
					///	 * <returns> �۲���Ա��ְ�� </returns>
					///	 
						virtual public std::string getJob() = 0;
					};
				}
			}
		}
	}
}