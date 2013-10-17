#pragma once

#include "Colleague.h"
#include "Mediator.h"
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
					/// <summary> * CPU�࣬һ��ͬ���� </summary>
					/// 
					class CPU : public Colleague
					{
					public:
						CPU(Mediator *mediator);
					///	
					///	 <summary> * �ֽ��������Ƶ���� </summary>
					///	 
					private:
						std::string videoData;
					///	
					///	 <summary> * �ֽ�������������� </summary>
					///	 
						std::string soundData;
					///	
					///	 <summary> * ��ȡ�ֽ��������Ƶ���� </summary>
					///	 * <returns> �ֽ��������Ƶ���� </returns>
					///	 
					public:
						virtual std::string getVideoData();
					///	
					///	 <summary> * ��ȡ�ֽ�������������� </summary>
					///	 * <returns> �ֽ�������������� </returns>
					///	 
						virtual std::string getSoundData();
					///	
					///	 <summary> * �������ݣ������ݷֳ���Ƶ����Ƶ������ </summary>
					///	 * <param name="data"> ����������� </param>
					///	 
						virtual void executeData(std::string data);


					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								videoData = "";
								soundData = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}