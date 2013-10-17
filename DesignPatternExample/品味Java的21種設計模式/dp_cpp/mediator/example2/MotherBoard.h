#pragma once

#include "Mediator.h"
#include "CDDriver.h"
#include "CPU.h"
#include "VideoCard.h"
#include "SoundCard.h"
#include "Colleague.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example2
				{
					///
					/// <summary> * �����࣬ʵ���н��߽ӿ� </summary>
					/// 
					class MotherBoard : public Mediator
					{
					///	
					///	 <summary> * ��Ҫ֪��Ҫ������ͬ���ࡪ�������� </summary>
					///	 
					private:
						CDDriver *cdDriver;
					///	
					///	 <summary> * ��Ҫ֪��Ҫ������ͬ���ࡪ��CPU�� </summary>
					///	 
						CPU *cpu;
					///	
					///	 <summary> * ��Ҫ֪��Ҫ������ͬ���ࡪ���Կ��� </summary>
					///	 
						VideoCard *videoCard;
					///	
					///	 <summary> * ��Ҫ֪��Ҫ������ͬ���ࡪ�������� </summary>
					///	 
						SoundCard *soundCard;

					public:
						virtual void setCdDriver(CDDriver *cdDriver);
						virtual void setCpu(CPU *cpu);
						virtual void setVideoCard(VideoCard *videoCard);
						virtual void setSoundCard(SoundCard *soundCard);

						virtual void changed(Colleague *colleague);
					///	
					///	 <summary> * ���������ȡ���ݹ�������������Ľ��� </summary>
					///	 * <param name="cd"> ����ͬ�¶��� </param>
					///	 
					private:
						void opeCDDriverReadData(CDDriver *cd);
					///	
					///	 <summary> * ����CPU���������ݺ�����������Ľ��� </summary>
					///	 * <param name="cpu"> CPUͬ���� </param>
					///	 
						void opeCPU(CPU *cpu);

					};

				}
			}
		}
	}
}