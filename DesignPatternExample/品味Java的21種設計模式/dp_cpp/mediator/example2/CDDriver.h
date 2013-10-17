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
					/// <summary> * �����࣬һ��ͬ���� </summary>
					/// 
					class CDDriver : public Colleague
					{
					public:
						CDDriver(Mediator *mediator);
					///	
					///	 <summary> * ������ȡ���������� </summary>
					///	 
					private:
						std::string data;
					///	
					///	 <summary> * ��ȡ������ȡ���������� </summary>
					///	 * <returns> ������ȡ���������� </returns>
					///	 
					public:
						virtual std::string getData();
					///	
					///	 <summary> * ��ȡ���� </summary>
					///	 
						virtual void readCD();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								data = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}