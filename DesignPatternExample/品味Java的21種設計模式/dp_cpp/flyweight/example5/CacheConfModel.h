#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example5
				{
					///
					/// <summary> * ������Ԫ���󻺴�����ö��� </summary>
					/// 
					class CacheConfModel
					{
					///	
					///	 <summary> * ���濪ʼ��ʱ�Ŀ�ʼʱ�� </summary>
					///	 
					private:
						long long beginTime;
					///	
					///	 <summary> * ��������ŵĳ���ʱ�䣬��ʵ�������ʹ�õ�ʱ�� </summary>
					///	 
						double durableTime;
					///	
					///	 <summary> * ���������Ҫ�����ô洢��Ҳ���ǲ���Ҫ�ӻ�����ɾ�� </summary>
					///	 
						bool forever;
					public:
						virtual bool isForever();
						virtual void setForever(bool forever);
						virtual long long getBeginTime();
						virtual void setBeginTime(long long beginTime);
						virtual double getDurableTime();
						virtual void setDurableTime(double durableTime);
					};
				}
			}
		}
	}
}