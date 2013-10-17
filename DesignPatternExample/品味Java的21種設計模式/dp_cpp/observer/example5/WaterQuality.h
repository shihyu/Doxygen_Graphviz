#pragma once

#include "WaterQualitySubject.h"
#include "WatcherObserver.h"
#include <string>
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
					/// <summary> * �����ˮ�ʼ����� </summary>
					/// 
					class WaterQuality : public WaterQualitySubject
					{
					///	
					///	 <summary> * ��Ⱦ�ļ���0��ʾ������1��ʾ�����Ⱦ��2��ʾ�ж���Ⱦ��3��ʾ�߶���Ⱦ </summary>
					///	 
					private:
						int polluteLevel;
					///	
					///	 <summary> * ��ȡˮ����Ⱦ�ļ��� </summary>
					///	 * <returns> ˮ����Ⱦ�ļ��� </returns>
					///	 
					public:
						virtual int getPolluteLevel();
					///	
					///	 <summary> * �����ˮ�����������ˮ����Ⱦ�ļ��� </summary>
					///	 * <param name="polluteLevel"> ˮ����Ⱦ�ļ��� </param>
					///	 
						virtual void setPolluteLevel(int polluteLevel);
					///	
					///	 <summary> * ֪ͨ��Ӧ�Ĺ۲��߶��� </summary>
					///	 
						virtual void notifyWatchers();
					};
				}
			}
		}
	}
}